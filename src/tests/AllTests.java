package tests;

import main.FriendTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LinkTest.class, SocialNetworkTest.class, UserTest.class, FriendTest.class })
public class AllTests {

}
