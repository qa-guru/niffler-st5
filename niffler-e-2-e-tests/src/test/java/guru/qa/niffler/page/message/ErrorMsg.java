package guru.qa.niffler.page.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorMsg implements Msg {
  BAD_CREDENTIALS("Bad credentials"),
  CAN_NOT_ADD_CATEGORY("Can not add new category"),
  PASSWORDS_SHOULD_BE_EQUAL("Passwords should be equal"),
  CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING("You can not pick future date"),
  CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY("Category is required"),
  CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT("Amount is required");

  private final String msg;

  @Override
  public String getMessage() {
    return msg;
  }
}
