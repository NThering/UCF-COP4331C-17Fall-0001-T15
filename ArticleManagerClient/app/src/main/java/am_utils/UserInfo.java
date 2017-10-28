package am_utils;

public class UserInfo
{
    /** Name the user uses to login, and also what is displayed to everyone else.  All Usernames must be unique. */
    String username;

    /** Set of bitflags that determine what the user is permitted to do. For now, P_ALL is the only one and is used to mark admins.  Normal users should have 0 for this value.*/
    int permissions;
}