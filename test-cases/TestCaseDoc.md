## Login

**Assumptions**
- User001 exists in the accounts file with type FS, and credit 100.00, and user User999 does not exist.
- Rejected or incomplete transactions are not written to the transaction file.
- The end-of-session (00) record holds the logged-in user's name, type and credit.
- After a rejected login, the Front End returns to the login prompt.
- The name END is reserved for the file terminator and cannot be a valid user.

| Test Name | Input File | Intention |
|---|---|---|
| Login_01_valid | inputs/login/Login_01_valid.txt | An existing user can log in, and logout writes the transaction file with only the 00 record. |
| Login_02_invalid_username | inputs/login/Login_02_invalid_username.txt | A login with an unknown username (User999) is rejected and not recorded. A valid login afterwards still works. |
| Login_03_transaction_before_login | inputs/login/Login_03_transaction_before_login.txt | Sell, buy and create are rejected before login and not recorded in the transaction file. |
| Login_04_already_logged_in | inputs/login/Login_04_already_logged_in.txt | A login transaction entered during an active session is rejected, and the original session continues until logout. |
| Login_05_admin_login | inputs/login/Login_05_admin_login.txt | An admin user can log in, and the end-of-session record shows the AA user type. |
| Login_06_case_sensitive_username | inputs/login/Login_06_case_sensitive_username.txt | Usernames are case sensitive, so "user001" is rejected. A valid login afterwards still works. |
| Login_07_username_16_chars | inputs/login/Login_07_username_16_chars.txt | A 16-character username, over the 15-character limit, is rejected. A valid login afterwards still works. |
| Login_08_non_admin_privileged_rejected | inputs/login/Login_08_non_admin_privileged_rejected.txt | After a non-admin login, the privileged transactions create, delete and refund are all rejected and not recorded. |
| Login_09_username_with_spaces | inputs/login/Login_09_username_with_spaces.txt | A username containing a space ("User 001", "     ") is not an existing user and is rejected. A valid login afterwards still works. |



## Logout

**Assumptions**
- User001 exists in the accounts file (type FS, credit 100.00).
- Rejected or incomplete transactions are not written to the transaction file.
- The end-of-session (00) record holds the logged-in user's name, type and credit at logout.
- An accepted addcredit record (06) shows the account's credit after the addition.

| Test Name | Input File | Intention |
|---|---|---|
| Logout_01_valid | inputs/logout/Logout_01_valid.txt | A logged-in user can log out, and the transaction file is written with the 00 end-of-session record. |
| Logout_02_not_logged_in | inputs/logout/Logout_02_not_logged_in.txt | A logout before any login is rejected and not recorded. A valid login and logout afterwards still work. |
| Logout_03_invalid_state | inputs/logout/Logout_03_invalid_state.txt | After a logout, sell, buy and create are rejected because only login is accepted, and none are recorded. |
| Logout_04_writes_transactions | inputs/logout/Logout_04_writes_transactions.txt | Logout writes every accepted transaction of the session (here an addcredit) to the transaction file, followed by the 00 record. |



## Add Credit

**Assumptions**
- User001 (FS, 100.00), Admin001 (AA, 1000.00), User002 (FS, 999999.50). User999 does not exist.
- Rejected transactions are not written to the transaction file. Based on client response.
- The 06 record shows the credited account's name, type and credit after the addition. Team decision, neater output
- In admin mode the Front End asks for the amount first, then the username. from project docs
- The $1000.00 limit is per account per session (project docs) and cumulative across multiple addcredit transactions in that session. client left it open ended so this felt easier.
- Zero and negative amounts are rejected. from client answers
- Credit may never exceed 999999.99. from client answers.

| Test Name | Input File | Intention |
|---|---|---|
| AddCredit_01_valid | inputs/add-credit/AddCredit_01_valid.txt | A valid amount is added to a standard user's account and recorded as an 06 record. |
| AddCredit_02_zero | inputs/add-credit/AddCredit_02_zero.txt | A zero amount is rejected and not recorded. |
| AddCredit_03_negative | inputs/add-credit/AddCredit_03_negative.txt | A negative amount is rejected and not recorded. |
| AddCredit_04_overflow | inputs/add-credit/AddCredit_04_overflow.txt | An amount just over the $1000.00 session limit (1000.01) is rejected and not recorded. |
| AddCredit_05_maximum_value | inputs/add-credit/AddCredit_05_maximum_value.txt | An amount exactly at the $1000.00 session limit is accepted and recorded. |
| AddCredit_06_session_limit_cumulative | inputs/add-credit/AddCredit_06_session_limit_cumulative.txt | Two additions of 600.00 in one session: the first is accepted, the second is rejected because the session total would pass $1000.00. |
| AddCredit_07_admin_valid | inputs/add-credit/AddCredit_07_admin_valid.txt | An admin can add credit to an existing user's account by entering the amount and then the username. |
| AddCredit_08_admin_nonexistent_user | inputs/add-credit/AddCredit_08_admin_nonexistent_user.txt | An admin adding credit to a username that does not exist is rejected and not recorded. |
| AddCredit_09_non_numeric_amount | inputs/add-credit/AddCredit_09_non_numeric_amount.txt | A non-numeric amount is rejected, and is not recorded. |
| AddCredit_10_account_overflow | inputs/add-credit/AddCredit_10_account_overflow.txt | An addition that would push an account past 999999.99 is rejected and not recorded. |



## Buy

**Assumptions**
- Accounts: User001 (FS, 100.00), Admin001 (AA, 1000.00), Seller001 (SS, 0.00), Seller002 (SS, 999999.50), Buyer001 (BS, 15.00). 
- Available games: Game001 (Seller001, 15.00), Game002 (Seller001, 20.00), Game003 (Seller001, 500.00), Game004 (Seller002, 15.00), Game005 (User001, 15.00). User001 already owns Game002.
- The Front End asks for the game name, then the seller's username. from the requirements doc
- Buy is accepted for every user type except sell-standard. requirements doc, also some client answers
- Rejected transactions are not written to the transaction file. client answers
- The 04 record holds the game name, seller, buyer and price. The game name field is padded to 26 characters. from requirements doc based on client answer.
- The game name and seller must match an existing listing. a game listed by a different seller is rejected. client answer, up to team
- A purchase that would push the seller's credit over 999999.99 is rejected. client answer.
- A user cannot buy their own listing. client leaves it up to team

| Test Name | Input File | Intention |
|---|---|---|
| Buy_01_valid | inputs/buy/Buy_01_valid.txt | A full-standard user with enough credit buys an available game, and an 04 record is written. |
| Buy_02_game_not_found | inputs/buy/Buy_02_game_not_found.txt | Buying a game that does not exist is rejected and not recorded. |
| Buy_03_insufficient_credit | inputs/buy/Buy_03_insufficient_credit.txt | Buying a game that costs more than the buyer's credit is rejected and not recorded. |
| Buy_04_ownership_restriction | inputs/buy/Buy_04_ownership_restriction.txt | Buying a game the buyer already owns is rejected and not recorded. |
| Buy_05_role_restriction | inputs/buy/Buy_05_role_restriction.txt | A sell-standard user cannot buy, the buy transaction is rejected and not recorded. |
| Buy_06_admin_valid | inputs/buy/Buy_06_admin_valid.txt | An admin is not a sell-standard user, so an admin purchase is accepted and recorded. |
| Buy_07_buy_standard_exact_credit | inputs/buy/Buy_07_buy_standard_exact_credit.txt | A buy-standard user whose credit equals the game price can buy it, leaving 0.00. |
| Buy_08_wrong_seller | inputs/buy/Buy_08_wrong_seller.txt | A real game paired with a seller who is not its seller is rejected and not recorded. |
| Buy_09_nonexistent_seller | inputs/buy/Buy_09_nonexistent_seller.txt | A seller username that does not exist is rejected and not recorded. |
| Buy_10_seller_credit_overflow | inputs/buy/Buy_10_seller_credit_overflow.txt | A purchase that would push the seller's credit over 999999.99 is rejected and not recorded. |
| Buy_11_buy_own_game | inputs/buy/Buy_11_buy_own_game.txt | A user trying to buy their own listing is rejected and not recorded. |
| Buy_12_buy_same_game_twice | inputs/buy/Buy_12_buy_same_game_twice.txt | The first purchase is accepted, and a second purchase of the same game is rejected as already owned. |



## Create

**Assumptions**
- Accounts: User001 (FS, 100.00), Admin001 (AA, 1000.00).
- The Front End asks for the new username first, then the user type (requirements doc). The tester types the type as a two-letter code: AA, FS, BS or SS. seemed to make the most sense
- Create is a privileged transaction, accepted only for admin users (requirements doc).
- New usernames are at most 15 characters and must differ from all current users (requirements doc). Usernames are case sensitive (client), so "user001" is different from "User001".
- A new user starts with 0.00 credit (client), so the 01 record shows 000000.00.
- A rejected username ends the create transaction without asking for a type. A rejected type ends it after both prompts. Rejected transactions are not recorded (client).
- - A username cannot be created twice in the same session (requirements doc).
- The 00 record holds the logged-in user's name, type and credit.

| Test Name | Input File | Intention |
|---|---|---|
| Create_01_valid | inputs/create/Create_01_valid.txt | An admin creates a full-standard user, and a 01 record with 0.00 credit is written. |
| Create_02_non_admin | inputs/create/Create_02_non_admin.txt | A non-admin user cannot create: the transaction is rejected and not recorded. |
| Create_03_duplicate_username | inputs/create/Create_03_duplicate_username.txt | Creating a user whose name already exists (User001) is rejected and not recorded. |
| Create_04_username_15_chars | inputs/create/Create_04_username_15_chars.txt | A username of exactly 15 characters is accepted and fills the record's username field. |
| Create_05_username_16_chars | inputs/create/Create_05_username_16_chars.txt | A username of 16 characters, over the limit, is rejected and not recorded. |
| Create_06_invalid_user_type | inputs/create/Create_06_invalid_user_type.txt | An invalid user type (XX) is rejected and the user is not recorded. |
| Create_07_initial_credit_zero | inputs/create/Create_07_initial_credit_zero.txt | A new buy-standard user starts with 0.00 credit (client Q2), shown in the 01 record. |
| Create_08_admin_type | inputs/create/Create_08_admin_type.txt | An admin can create another admin (type AA). |
| Create_09_sell_standard_type | inputs/create/Create_09_sell_standard_type.txt | An admin can create a sell-standard user (type SS). |
| Create_10_case_sensitive_username | inputs/create/Create_10_case_sensitive_username.txt | "user001" differs from "User001" because usernames are case sensitive (client Q10.2), so it is accepted. |
| Create_11_empty_username | inputs/create/Create_11_empty_username.txt | An empty username is rejected gracefully without crashing, and is not recorded. |
| Create_12_duplicate_in_session | inputs/create/Create_12_duplicate_in_session.txt | Creating the same new username twice in one session is rejected the second time. |
| Create_13_username_END | inputs/create/Create_13_username_END.txt | END is the account file terminator, so it is rejected as a username. |



## Delete

**Assumptions**
- Accounts: User001 (FS, 100.00), Admin001 (AA, 1000.00), Seller001 (SS, 0.00) . User999 does not exist.
- Games: Game001 listed for sale at 15.00, from user Seller001.
- The Front End asks only for the username to delete (requirements doc).
- Delete is a privileged transaction, accepted only for admin users (requirements doc).
- The username must belong to an existing user (requirements doc). Usernames are case sensitive (client).
- - An admin can delete their own account (client). This conflicts with the requirements doc, which says the username must not be the current user. We follow the client. Deleting your own account removes you from the user list and ends the session immediately, as if logout had been entered, and the transaction file is written. Client left specifics up to us.
- Deleting a user cancels their games for sale, so a deleted user's games can no longer be bought (requirements doc). Everything attached to the user is deleted with them (client).
- The 02 record holds the deleted user's name, type and credit at the time of deletion. Team decision
- Rejected transactions are not written to the transaction file (client).

| Test Name | Input File | Intention |
|---|---|---|
| Delete_01_valid | inputs/delete/Delete_01_valid.txt | An admin deletes an existing user, and a 02 record is written. |
| Delete_02_non_admin | inputs/delete/Delete_02_non_admin.txt | A non-admin cannot delete: the transaction is rejected and not recorded. |
| Delete_03_delete_self | inputs/delete/Delete_03_delete_self.txt | An admin deleting their own account is accepted per the client (Q10.3), despite the doc saying not the current user. Documents a requirements conflict. |
| Delete_04_nonexistent_user | inputs/delete/Delete_04_nonexistent_user.txt | Deleting a username that does not exist is rejected and not recorded. |
| Delete_05_deleted_user_with_games | inputs/delete/Delete_05_deleted_user_with_games.txt | After a seller is deleted, their games for sale can no longer be bought, so the purchase is rejected. |
| Delete_06_username_with_spaces | inputs/delete/Delete_06_username_with_spaces.txt | A username with spaces is rejected, and is not recorded. |
| Delete_07_case_sensitive_username | inputs/delete/Delete_07_case_sensitive_username.txt | "user001" is not the same user as "User001" (client), so the delete is rejected. |
| Delete_08_delete_twice | inputs/delete/Delete_08_delete_twice.txt | After a user is deleted, deleting the same username again is rejected. Only one 02 record is written. |



## List

**Assumptions**
- Accounts: User001 (FS, 100.00), Admin001 (AA, 1000.00), Seller001 (SS, 0.00).
- Available games: Game001 (Seller001, 15.00), Game002 (Seller001, 20.00), Game003 (Seller001, 500.00), Game004 (Seller002, 15.00), Game005 (User001, 15.00).
- The list transaction is not explicitly defined in the requirements document. Its constraints come from the Available Games File and Game Collection File requirements, based on the client's response.
- The list displays available games using the Available Games File format: game name, seller username and price, with the field lengths and padding taken from the project examples. Client confirmed the example length should be used where the document is inconsistent.
- List is available to all logged-in user types because the client did not specify a role restriction.
- Games are displayed in file order. The client stated that the order is up to the team.
- List does not write a transaction record to the transaction file because no transaction code for list is defined in the requirements document.
- Rejected transactions are not written to the transaction file. Based on client response.
- A transaction other than login is rejected before a user logs in. Based on the project requirements.

| Test Name | Input File | Intention |
|---|---|---|
| List_01_display_available_games | inputs/list/List_01_display_available_games.txt | A logged-in full-standard user can list the available games, including each game's seller and price. |
| List_02_admin_can_list_games | inputs/list/List_02_admin_can_list_games.txt | An admin user can list the available games. |
| List_03_rejected_sell_does_not_change_list | inputs/list/List_03_rejected_sell_does_not_change_list.txt | A rejected sell transaction does not add a game to the available games list or write a transaction record. |
| List_04_list_before_login | inputs/list/List_04_list_before_login.txt | A list transaction before login is rejected and is not written to the transaction file. |
| List_05_sell_standard_can_list_games | inputs/list/List_05_sell_standard_can_list_games.txt | A sell-standard user can list the available games. |



## Refund

**Assumptions**
- Accounts: User001 (FS, 100.00), Admin001 (AA, 1000.00), Seller001 (SS, 0.00), User999 does not exist.
- Games: Game001 listed for sale at 15.00 from Seller001.
- Refund is a privileged transaction and is accepted only when logged in as an admin user (requirements doc).
- The Front End asks for the buyer username, seller username and amount of credit to transfer (requirements doc).
- Buyer and seller must both be current users (requirements doc).
- The refund transfers the specified amount from the seller's credit balance to the buyer's credit balance (requirements doc).
- A refund removes the purchased game from the buyer's collection (client).
- The buyer can purchase the game again after a refund (client).
- Zero and negative refund amounts are rejected (client).
- The seller must have enough credit for the refund (client).
- Rejected transactions are not written to the transaction file.
- A successful refund is written using transaction code 05 with the buyer username, seller username and refund amount (requirements doc).
- Alphabetic fields in the transaction file are left justified and filled with spaces, and monetary values use the required `.00` format (requirements doc).

| Test Name | Input File | Intention |
|---|---|---|
| Refund_01_valid | inputs/refund/Refund_01_valid.txt | An admin successfully refunds credit from a current seller to a current buyer, and a 05 refund record is written. |
| Refund_02_non_admin | inputs/refund/Refund_02_non_admin.txt | A non-admin user cannot perform the privileged refund transaction. |
| Refund_03_buyer_nonexistent | inputs/refund/Refund_03_buyer_nonexistent.txt | A refund is rejected when the buyer is not a current user. |
| Refund_04_seller_nonexistent | inputs/refund/Refund_04_seller_nonexistent.txt | A refund is rejected when the seller is not a current user. |
| Refund_05_zero | inputs/refund/Refund_05_zero.txt | A refund with a zero amount is rejected. |
| Refund_06_negative | inputs/refund/Refund_06_negative.txt | A refund with a negative amount is rejected. |
| Refund_07_insufficient_seller_credit | inputs/refund/Refund_07_insufficient_seller_credit.txt | A refund is rejected when the seller does not have enough credit to transfer the requested amount. |
| Refund_08_removes_game | inputs/refund/Refund_08_removes_game.txt | After a purchase and refund, the purchased game is removed from the buyer's collection. |
| Refund_09_rebuy_after_refund | inputs/refund/Refund_09_rebuy_after_refund.txt | After a purchase and refund, the buyer can purchase the same game again. |



## Sell

**Assumptions**

- Accounts: User001 (FS, 100.00), Admin001 (AA, 1000.00), Seller001 (SS, 0.00), Buyer001 (BS, 15.00), User999 does not exist.
- Available games: Game001 (Seller001, 15.00), Game002 (Seller001, 20.00), Game003 (Seller001, 500.00), Game004 (Seller002, 15.00), Game005 (User001, 15.00).
- Sell is a semi-privileged transaction and is accepted for any account type except buy-standard.
- The logged-in user is the seller for a sell transaction.
- The Front End asks for the game name and price.
- A new game name must not already exist in the available games file.
- The maximum game price is 999.99.
- The maximum game name length is 25 characters.
- A successful sell is written to the daily transaction file using transaction code 03 with the game name, seller username and price.
- A new game cannot be involved in another transaction during the same session after it is successfully put up for sale.
- Rejected transactions are not written to the transaction file.
- The Front End must gracefully handle invalid input without crashing.
- User999 is not an existing account, so login as User999 is rejected before the sell transaction can be processed.
- Zero and negative prices are treated as invalid input for robustness testing.
- A game name containing invalid input such as `/` is treated as invalid input for robustness testing.

| Test Name | Input File | Intention |
|---|---|---|
| Sell_01_valid | inputs/sell/Sell_01_valid.txt | A valid sell transaction creates a new game listing and writes a 03 sell transaction to the daily transaction file. |
| Sell_02_invalid_price | inputs/sell/Sell_02_invalid_price.txt | An invalid non-numeric price is rejected without crashing and without writing a sell transaction. |
| Sell_03_game_name_25_chars | inputs/sell/Sell_03_game_name_25_chars.txt | A game name containing exactly 25 characters is accepted and the game is successfully listed for sale. |
| Sell_04_game_name_26_chars | inputs/sell/Sell_04_game_name_26_chars.txt | A game name containing 26 characters exceeds the maximum length and is rejected without writing a sell transaction. |
| Sell_05_duplicate_game | inputs/sell/Sell_05_duplicate_game.txt | A sell transaction using a game name that already exists is rejected and does not create a duplicate listing. |
| Sell_06_role_restriction | inputs/sell/Sell_06_role_restriction.txt | A buy-standard user is not permitted to perform the semi-privileged sell transaction. |
| Sell_07_nonexistent_user | inputs/sell/Sell_07_nonexistent_user.txt | A nonexistent user cannot log in and therefore cannot perform a sell transaction. |
| Sell_08_zero_price | inputs/sell/Sell_08_zero_price.txt | A zero price is rejected as invalid input without writing a sell transaction. |
| Sell_09_negative_price | inputs/sell/Sell_09_negative_price.txt | A negative price is rejected as invalid input without writing a sell transaction. |
| Sell_10_invalid_game_name | inputs/sell/Sell_10_invalid_game_name.txt | Invalid game-name input is rejected without crashing and without writing a sell transaction. |