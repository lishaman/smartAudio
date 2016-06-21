Integration notes on Android:

1. Copy com.broadcom.cooee.jar and libcooee.so to your project, with below directory structure:
    libs/
    |-- armeabi
    |   |-- libcooee.so
    |-- com.broadcom.cooee.jar

2. Add following permissions:
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.GET_TASKS" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>

3. In java code:
    import com.broadcom.cooee.*;

    // set packet interval to 10ms. (default 8ms)
    //Cooee.SetPacketInterval(10);

    // send SSID and password
    Cooee.send("BroadcomAP", "12345678"); 

    // send with IP
    Cooee.send("BroadcomAP", "12345678", 0x0101a8c0); 

    // if you want to specify encryption key, use this:
    Cooee.send("BroadcomAP", "12345678", 0x0101a8c0, "Today is Friday?"); 

Note1: If you set encryption key, you must also set the same decryption key in target device.
Note2: Since above 2 calls block, note to put it in a standalone thread.
Note3: ip is in network order, so if you pass "192.168.1.1", it should be 0x0101a8c0

4. Class description:
public class Cooee {
	public static int send(String ssid, String password);
	public static int send(String ssid, String password, int ip);
	public static int send(String ssid, String password, int ip, String encryptionKey); 
	public static int SetPacketInterval(int ms);
};
