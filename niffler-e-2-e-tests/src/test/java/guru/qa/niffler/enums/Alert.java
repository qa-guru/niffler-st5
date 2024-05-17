package guru.qa.niffler.enums;

public enum Alert {
	INVITATION_IS_SENT("Invitation is sent"),
	INVITATION_IS_ACCEPTED("Invitation is accepted"),
	FRIEND_IS_DELETED("Friend is deleted"),
	INVITATION_IS_DECLINED("Invitation is declined");

	private final String value;

	Alert(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
