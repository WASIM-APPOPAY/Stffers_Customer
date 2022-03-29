package com.stuffer.stuffers.activity.contact;

public class SortedContacts implements Comparable<Contact> {
    String name;
    String ph_number;

    public SortedContacts(String name, String ph_number) {
        this.name = name;
        this.ph_number = ph_number;
    }

    public String getName() {
        return name;
    }

    public String getPh_number() {
        return ph_number;
    }

    @Override
    public int compareTo(Contact contact) {
        return 0;
    }
}
