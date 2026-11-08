#!/usr/bin/env bash

set -u

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
INPUT_DIR="$ROOT_DIR/tests/inputs"
OUTPUT_DIR="$ROOT_DIR/tests/output"
EXECUTABLE="$ROOT_DIR/distribution-system.exe"

ACCOUNTS="$ROOT_DIR/data/currentaccounts.txt"
GAMES="$ROOT_DIR/data/availablegames.txt"
COLLECTION="$ROOT_DIR/data/gamescollection.txt"

if [[ ! -x "$EXECUTABLE" ]]; then
    echo "ERROR: Executable not found or not executable: $EXECUTABLE"
    exit 1
fi

for FILE in "$ACCOUNTS" "$GAMES" "$COLLECTION"; do
    if [[ ! -f "$FILE" ]]; then
        echo "ERROR: Required data file not found: $FILE"
        exit 1
    fi
done

mkdir -p "$OUTPUT_DIR"

TOTAL=0
FAILED=0

while IFS= read -r INPUT_FILE; do
    RELATIVE_PATH="${INPUT_FILE#"$INPUT_DIR"/}"
    CATEGORY="$(dirname "$RELATIVE_PATH")"
    FILENAME="$(basename "$INPUT_FILE")"
    TEST_NAME="${FILENAME%.txt}"

    EXPECTED_OUT="$ROOT_DIR/tests/expected/$CATEGORY/$TEST_NAME.out"
    EXPECTED_ATF="$ROOT_DIR/tests/expected/$CATEGORY/$TEST_NAME.atf"

    ACTUAL_DIR="$OUTPUT_DIR/$CATEGORY"
    ACTUAL_OUT="$ACTUAL_DIR/$TEST_NAME.out"
    ACTUAL_ATF="$ACTUAL_DIR/$TEST_NAME.atf"

    mkdir -p "$ACTUAL_DIR"

    rm -f "$ACTUAL_OUT" "$ACTUAL_ATF"

    TOTAL=$((TOTAL + 1))

    echo "Running: $CATEGORY/$TEST_NAME"

    if [[ ! -f "$EXPECTED_OUT" || ! -f "$EXPECTED_ATF" ]]; then
        echo "  ERROR: Missing expected output file."
        FAILED=$((FAILED + 1))
        continue
    fi

    # Create an isolated working directory for this test.
    TEST_WORK_DIR="$(mktemp -d)"

    if [[ ! -d "$TEST_WORK_DIR" ]]; then
        echo "  ERROR: Could not create temporary directory."
        FAILED=$((FAILED + 1))
        continue
    fi

    # Create the data directory used by the executable arguments.
    mkdir -p "$TEST_WORK_DIR/data"

    # Copy original data into the isolated directory.
    cp "$ACCOUNTS" "$TEST_WORK_DIR/data/currentaccounts.txt"
    cp "$GAMES" "$TEST_WORK_DIR/data/availablegames.txt"
    cp "$COLLECTION" "$TEST_WORK_DIR/data/gamescollection.txt"

    # Create copies for the Windows-style paths used in the C++ files.
    # On Linux, these are literal filenames in the working directory.
    cp "$ACCOUNTS" "$TEST_WORK_DIR/.\\data\\currentaccounts.txt"
    cp "$GAMES" "$TEST_WORK_DIR/.\\data\\availablegames.txt"
    cp "$COLLECTION" "$TEST_WORK_DIR/.\\data\\gamescollection.txt"

    cp "$ACCOUNTS" "$TEST_WORK_DIR/.\\data\\temp_accounts.txt"
    cp "$GAMES" "$TEST_WORK_DIR/.\\data\\temp_availablegames.txt"

    : > "$TEST_WORK_DIR/.\\data\\temp.txt"

    # Run the test from its isolated working directory.
    (
        cd "$TEST_WORK_DIR" || exit 1

        "$EXECUTABLE" \
            "$TEST_WORK_DIR/data/currentaccounts.txt" \
            "$TEST_WORK_DIR/data/availablegames.txt" \
            "$TEST_WORK_DIR/data/gamescollection.txt" \
            "$ACTUAL_ATF" \
            < "$INPUT_FILE" \
            > "$ACTUAL_OUT" 2>&1
    )

    EXIT_CODE=$?

    # Remove all temporary data after the test.
    rm -rf "$TEST_WORK_DIR"

    if [[ $EXIT_CODE -ne 0 ]]; then
        echo "  ERROR: Program exited with code $EXIT_CODE."
        FAILED=$((FAILED + 1))
    else
        echo "  Execution completed."
    fi

done < <(find "$INPUT_DIR" -type f -name "*.txt" | sort)

echo
echo "================================"
echo "Test execution summary"
echo "================================"
echo "Total tests: $TOTAL"
echo "Execution failures: $FAILED"
echo "Actual outputs: $OUTPUT_DIR"
echo "================================"