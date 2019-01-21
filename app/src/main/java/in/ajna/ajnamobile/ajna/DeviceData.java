package in.ajna.ajnamobile.ajna;

public class DeviceData {

    private int armedStatus,
    detectedStatus,
    batteryStatus,
    sensorStatus,
    internetStatus;

    public DeviceData(){
    }
    public int getArmedStatus() { return armedStatus; }

    public void setArmedStatus(int armedStatus) { this.armedStatus = armedStatus; }

    public int getDetectedStatus() { return detectedStatus; }

    public void setDetectedStatus(int detectedStatus) { this.detectedStatus = detectedStatus; }

    public int getBatteryStatus() { return batteryStatus; }

    public void setBatteryStatus(int batteryStatus) { this.batteryStatus = batteryStatus; }

    public int getSensorStatus() { return sensorStatus; }

    public void setSensorStatus(int sensorStatus) { this.sensorStatus = sensorStatus; }

    public int getInternetStatus() { return internetStatus; }

    public void setInternetStatus(int internetStatus) { this.internetStatus = internetStatus; }
}
