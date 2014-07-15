package pl.spychalski.WeatherStation;

import android.content.Context;

import java.io.Serializable;

public class DayForecastSimple implements Serializable {

    private static final long serialVersionUID = 547998742264L;

    String WeatherIcon;
    String TempMax;
    String TempMin;
    String Date;
    String DayOfWeek;
    String TempDay;
    String TempNight;
    String WeatherCode;
    String Clouds;
    String Humidity;
    String Pressure;
    String WindSpeed;
    String WindDirection;
    String Rain;
    String Snow;

    public String getWeatherCode() {
        return WeatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        WeatherCode = weatherCode;
    }

    public String getClouds() {
        return Clouds;
    }

    public void setClouds(String clouds) {
        Clouds = clouds;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }

    public String getPressure() {
        return Pressure;
    }

    public void setPressure(String pressure) {
        Pressure = pressure;
    }

    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        WindSpeed = windSpeed;
    }

    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }

    public String getRain() {
        return Rain;
    }

    public void setRain(String rain) {
        Rain = rain;
    }

    public String getSnow() {
        return Snow;
    }

    public void setSnow(String snow) {
        Snow = snow;
    }

    public DayForecastSimple() {

    }

    public String getWeatherIcon() {
        return WeatherIcon;
    }

    public int getWeatherIconResource(Context context) {
        return Utils.getImageIdentifier(context, "icon_" + WeatherIcon);
    }

    public int getInvertedWeatherIconResource(Context context) {
        return Utils.getImageIdentifier(context, "icon_" + WeatherIcon + "_i");
    }

    public String getTranslatedDayOfWeek(Context context) {
        return context.getString(Utils.getStringIdentifier(context, "dayOfWeek_" + DayOfWeek));
    }

    public void setWeatherIcon(String weatherIcon) {
        WeatherIcon = weatherIcon;
    }

    public String getTempMax() {
        return TempMax;
    }

    public int getIntTempMax() {
        return Math.round(Float.parseFloat(TempMax));
    }

    public Float getFloatTempMax() {
        return Float.parseFloat(TempMax);
    }

    public void setTempMax(String tempMax) {
        TempMax = tempMax;
    }

    public String getTempMin() {
        return TempMin;
    }

    public void setTempMin(String tempMin) {
        TempMin = tempMin;
    }

    public int getIntTempMin() {
        return Math.round(Float.parseFloat(TempMin));
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public String getTempDay() {
        return TempDay;
    }

    public int getIntTempDay() {
        return Math.round(Float.parseFloat(TempDay));
    }

    public void setTempDay(String tempDay) {
        TempDay = tempDay;
    }

    public String getTempNight() {
        return TempNight;
    }

    public void setTempNight(String tempNight) {
        TempNight = tempNight;
    }

}
