package in.gov.cgg.alumni;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.gov.cgg.alumni.activities.NotifyModel;

public class GlobalDeclaration {
    public static final String BASEURL = "https://fcm.googleapis.com/fcm/";
    public static Details profileDetails;
    public static boolean title=false;
    public static List<Details> tempList=new ArrayList<>();
    public static String mobileNumber;
    //public static String type;
    public static int id;
    public static String MYKEY = "key=AAAAfz0lo4s:APA91bHFhL4oSq_SsJ-ITkOcbDg9QaAatwW-QPFRmrVna-whtp209zCtD0PMGzzQBf6rjLQPU5RxZpCr7lBQSM_aPlSRGhvkej4B69pG0JfR66wRocLeHqpZnUUytbcRP_A7cIBt8-b5";
    public static List<NotifyModel> notifyList;
    public static List<String> services;
    public static List<String> batcheslist;
    public static int position;
    public static Details details;
}
