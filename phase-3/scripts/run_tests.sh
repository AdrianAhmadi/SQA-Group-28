
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

    "$EXECUTABLE" "$ACCOUNTS" "$GAMES" "$COLLECTION" "$ACTUAL_ATF" \
        < "$INPUT_FILE" > "$ACTUAL_OUT" 2>&1

    EXIT_CODE=$?

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
