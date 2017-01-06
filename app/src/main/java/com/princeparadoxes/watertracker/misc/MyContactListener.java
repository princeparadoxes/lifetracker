package com.princeparadoxes.watertracker.misc;


import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

/**
 * Created by mfaella on 01/03/16.
 */
public class MyContactListener extends ContactListener {
    public void beginContact(Contact contact) {
        //Log.d("MyContactListener", "Begin contact");
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();
        Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
        WorldObject a = (WorldObject) userdataA,
                b = (WorldObject) userdataB;

    }
}
