package guru.qa.niffler.page.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SuccessMsg implements Msg {
  FRIEND_DELETED("Friend is deleted"),
  INVITATION_ACCEPTED("Invitation is accepted"),
  INVITATION_DECLINED("Invitation is declined"),
  SPENDING_ADDED("Spending successfully added"),
  PROFILE_UPDATED("Profile successfully updated");

  private final String msg;

  @Override
  public String getMessage() {
    return msg;
  }
}
