/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package am_utils;

/**
 *
 * @author NThering
 */
public class UserInfo 
{
    /** Name the user uses to login, and also what is displayed to everyone else.  All Usernames must be unique. */
    String Username;
    
    /** Set of bitflags that determine what the user is permitted to do. For now, P_ALL is the only one and is used to mark admins.  Normal users should have 0 for this value.*/
    int Permissions;
}