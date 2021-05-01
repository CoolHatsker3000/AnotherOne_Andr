package com.tryone.dyplomtest1.constants;

import com.tryone.dyplomtest1.R;

import java.util.HashMap;

public class Constants {
    public static final String TICKETS_KEY = "tickets";
    public static final String USERS_KEY="users";
    public static final String ADMIN_AREAS_KEY="adminAreas";
    public static final String LOCALITIES_KEY="localities";

    public static final String MAP_ADDRESS_EXTRA="address";
    public static final String MAP_COORDS_EXTRA_LATITUDE="latitude";
    public static final String MAP_COORDS_EXTRA_LONGITUDE="longitude";
    public static final String MAP_ADMIN_AREA_EXTRA="adminArea";
    public static final String MAP_LOCALITY_EXTRA="locality";


    public static final String[] STATUS_ARRAY = new String[]{"открыто", "принято", "рассматривается", "выполняется", "выполнено", "закрыто", "требуется доп информация"};
    public static final int[] STATUS_COLORS = new int [] {R.color.colorTicketOpen,R.color.colorTicketAccepted,R.color.colorTicketAssessment,R.color.colorTicketInProgress,R.color.colorTicketCompleted,R.color.colorTicketClosed,R.color.colorTicketAir};


}
