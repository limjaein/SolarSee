package com.example.jaein.solarsee;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import static android.graphics.Color.BLACK;
import static com.example.jaein.solarsee.LoginActivity.font;
import static java.lang.Integer.parseInt;


public class CustomMapFragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapClickListener{

    static String location;
    GoogleMap map;
    private MapView mapView;
    LatLng Center;
    ArrayList<PlaceInfo> m_data;
    Date date;
    String curDate, curTime;
    String sunriseInfo, sunsetInfo, rise_cloud, rise_temp, set_cloud, set_temp;
    int hour, min;
    SimpleDateFormat CurDateFormat, CurTimeFormat, CurHourFormat, CurMinFormat;

    public CustomMapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView)view.findViewById(R.id.map);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mapView != null){
            mapView.onCreate(savedInstanceState);
        }
        init();
    }

    public void init(){
        CurDateFormat = new SimpleDateFormat("yyyyMMdd");
        CurTimeFormat = new SimpleDateFormat("HHmm");
        CurHourFormat = new SimpleDateFormat("HH");
        CurMinFormat = new SimpleDateFormat("mm");
        date = new Date(System.currentTimeMillis());
        curDate = CurDateFormat.format(date);   //현재날짜
        hour = parseInt(CurHourFormat.format(date));
        min = parseInt(CurMinFormat.format(date));
        if(min<=40){
            if(hour==00){
                curDate = String.valueOf(parseInt(curDate)-1);
                hour = 23;
                curTime = String.valueOf(hour+""+min);
            }
            else{
                hour -= 1;
                if(hour>=0 && hour<=9){
                    curTime = String.valueOf("0"+hour+""+min);
                }
                else{
                    curTime = String.valueOf(hour+""+min);
                }

            }

        }
        if(hour>=20){
            curDate = String.valueOf(parseInt(curDate)+1);
        }


        Center = new LatLng(36.573497, 127.359965);
        m_data = new ArrayList<>();
        m_data.add(new PlaceInfo(new LatLng(37.691471, 129.032668),"정동진", 95, 130));
        m_data.add(new PlaceInfo(new LatLng(35.358903, 129.360385),"간절곶", 103, 80));
        m_data.add(new PlaceInfo(new LatLng(35.117180, 129.121676),"이기대", 99, 74));
        m_data.add(new PlaceInfo(new LatLng(33.458351, 126.942457),"성산일출봉", 60,37));
        m_data.add(new PlaceInfo(new LatLng(36.048384, 129.550187),"호미곶", 105, 95));
        m_data.add(new PlaceInfo(new LatLng(37.568087, 126.884900),"하늘공원", 58, 127));
        m_data.add(new PlaceInfo(new LatLng(36.502667, 126.337039),"꽃지해안공원", 49, 104));
        m_data.add(new PlaceInfo(new LatLng(35.684325, 126.531795),"변산반도", 52, 86));
        m_data.add(new PlaceInfo(new LatLng(34.303308, 126.527242),"땅끝마을", 53, 56));
        m_data.add(new PlaceInfo(new LatLng(34.593588, 127.802856),"향일암",75, 62));
        m_data.add(new PlaceInfo(new LatLng(35.338586, 127.731338),"지리산천왕봉", 74, 78));
        m_data.add(new PlaceInfo(new LatLng(37.098003, 128.915235),"태백산", 93, 117));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMapClickListener(this);

        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setAllGesturesEnabled(false);

        for(int i=0; i<m_data.size(); i++){
            LatLng latLng = m_data.get(i).getP_latlng();
            parsingGroup(latLng, curDate, m_data.get(i), curTime);
        }

        updateMap();
    }

    public void updateMap(){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Center,6.5f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Center, 6.5f));
    }

    public void parsingGroup(LatLng latlng, final String date, final PlaceInfo data, final String time){
        double par_lat = latlng.latitude;
        double par_log = latlng.longitude;

       Ion.with(this)
                .load("http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo?longitude="+par_log+
                        "&latitude="+par_lat+"&locdate="+date+"&dnYn=Y&" +
                        "ServiceKey=WKilB3x6pMR6xXG5MBPsHLE3Xxq48pi1S02fQjef%2FY83AadtPrPr9Wq1mhzsOsJ2efkrLD0i4KW8irbmFnhq0w%3D%3D")
                .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, final String result) {
                        Ion.with(CustomMapFragment.this)
                                .load("http://www.kma.go.kr/wid/queryDFS.jsp?gridx="+data.getP_x()+"&gridy="+data.getP_y())
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result2) {
                                        parsingXML(result);
                                        int sunRisehour = parseInt(sunriseInfo.substring(0,2));
                                        int sunSethour = parseInt(sunsetInfo.substring(0,2));
                                        parsingXML2(result2, sunRisehour, 0);
                                        parsingXML2(result2, sunSethour, 1);
                                        data.setSunInfo(sunriseInfo, sunsetInfo);
                                        data.setWeatherInfo(rise_temp, rise_cloud, set_temp, set_cloud);
                                        Marker mk = map.addMarker(new MarkerOptions()
                                                .position(data.getP_latlng())
                                                .title(data.getP_placeName())
                                                .snippet(data.getP_sunRise()+","+data.getP_sunSet()+","+data.getP_rcloud()+","+data.getP_rtemperature()+
                                                ","+data.getP_scloud()+","+data.getP_stemperature()));

                                        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                            @Override
                                            public void onInfoWindowClick(Marker marker) {
                                                location = marker.getTitle();
                                                String Tabalbum  = ((MainActivity)getActivity()).getAlbumFragmentB();

                                                AlbumFragment albumFrag = (AlbumFragment)getActivity().getSupportFragmentManager().findFragmentByTag(Tabalbum);

                                                albumFrag.locaImageList();

                                                ((MainActivity)getActivity()).getViewPager().setCurrentItem(1);

                                            }
                                        });

                                    }
                                });
                    }
                });

    }

    public void parsingXML(String result){
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance().newInstance();
            factory.setNamespaceAware(true);    //XML namespace 지원여부 설정
            XmlPullParser xpo = factory.newPullParser();
            xpo.setInput(new StringReader(result));
            int eventType = xpo.getEventType();
            int bSet = 0;

            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_DOCUMENT){
                    ;
                }
                else if(eventType == XmlPullParser.START_TAG){
                    String tag_name = xpo.getName();
                    if(tag_name.equals("sunrise") ){
                        bSet = 1;
                    }
                    else if(tag_name.equals("sunset") ){
                        bSet = 2;
                    }
                    else{
                        bSet = 0;
                    }

                }
                else if(eventType == XmlPullParser.TEXT){
                    if(bSet==1){
                        String hour = xpo.getText().substring(0,2);
                        String min = xpo.getText().substring(2,4);
                        String sec = xpo.getText().substring(4,6);
                        sunriseInfo = hour+"시"+min+"분"+sec+"초";
                        bSet = 0;
                    }
                    else if(bSet==2){
                        String hour = xpo.getText().substring(0,2);
                        String min = xpo.getText().substring(2,4);
                        String sec = xpo.getText().substring(4,6);
                        sunsetInfo = hour+"시"+min+"분"+sec+"초";
                        bSet = 0;
                    }
                }
                else if(eventType == XmlPullParser.END_TAG){
                    ;
                }
                try {
                    eventType = xpo.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    public void parsingXML2(String result, int time, int i){
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance().newInstance();
            factory.setNamespaceAware(true);    //XML namespace 지원여부 설정
            XmlPullParser xpo = factory.newPullParser();
            xpo.setInput(new StringReader(result));
            int eventType = xpo.getEventType();
            int bSet = 0;
            int cSet = 0;
            int dSet = 0;   //처음 시간 찾기 위한 표시

            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_DOCUMENT){
                    ;
                }
                else if(eventType == XmlPullParser.START_TAG){
                    String tag_name = xpo.getName();
                    if(tag_name.equals("hour") && dSet==0){
                        bSet = 1;
                    }
                    else if(tag_name.equals("temp") && dSet ==1 ){
                        bSet = 2;
                    }
                    else if(tag_name.equals("wfKor") && dSet ==1){
                        bSet = 3;
                    }
                    else{
                        bSet = 0;
                    }

                }
                else if(eventType == XmlPullParser.TEXT){
                    if(bSet==1){
                        if(isStringInt(xpo.getText())){
                            int hour = Integer.parseInt(xpo.getText());
                            if(hour-3<=time && time<hour){
                                dSet = 1;   //내가원하는온도찾음
                                cSet = 1;
                            }
                            else{
                                cSet = 0;
                            }
                        }
                    }
                    else if(bSet==2){
                        if(cSet == 1){
                            if(i==0){
                                rise_temp = "기온:"+xpo.getText();
                            }
                            else{
                                set_temp ="기온:"+xpo.getText();
                            }

                        }
                        bSet = 0;
                    }
                    else if(bSet == 3){
                        if(cSet == 1){
                            if(i == 0){
                                rise_cloud = xpo.getText();
                                cSet = 0;
                            }
                            else{
                                set_cloud = xpo.getText();
                                cSet = 0;
                            }
                        }
                        bSet = 0;
                    }
                }
                else if(eventType == XmlPullParser.END_TAG){
                    ;
                }
                try {
                    eventType = xpo.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public static boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getActivity().getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(final Marker marker, View view) {

            LinearLayout li = (LinearLayout)view.findViewById(R.id.linear);
            li.setBackgroundResource(R.drawable.custom_info_bubble);

            String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
            titleUi.setTypeface(font);
            titleUi.setTextSize(30);
            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            StringTokenizer tokens = new StringTokenizer(snippet);
            String c_sunRise = tokens.nextToken(",");
            String c_sunSet = tokens.nextToken(",");
            String c_rcloud = tokens.nextToken(",");
            String c_rtemper = tokens.nextToken(",");
            String c_scloud = tokens.nextToken(",");
            String c_stemper = tokens.nextToken(",");

            TextView snippetUi = ((TextView) view.findViewById(R.id.sunRiseTime));
            snippetUi.setTypeface(font);
            snippetUi.setTextSize(20);
            snippetUi.setTextColor(BLACK);
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(c_sunRise);
                snippetUi.setText(snippetText.subSequence(0,6));
            } else {
                snippetUi.setText("");
            }

            TextView snippetUi2 = ((TextView) view.findViewById(R.id.sunSetTime));
            snippetUi2.setTypeface(font);
            snippetUi2.setTextSize(20);
            snippetUi2.setTextColor(BLACK);
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(c_sunSet);
                snippetUi2.setText(snippetText.subSequence(0,6));
            } else {
                snippetUi2.setText("");
            }


            ImageView snippetUi3 = ((ImageView) view.findViewById(R.id.riseCloud));
            switch (c_rcloud){
                case "맑음":
                    snippetUi3.setImageResource(R.drawable.sun);
                    break;
                case "구름 조금":
                    snippetUi3.setImageResource(R.drawable.suncloud);
                    break;
                case "구름 많음":
                    snippetUi3.setImageResource(R.drawable.cloudy);
                    break;
                case "흐림":
                    snippetUi3.setImageResource(R.drawable.cloudy);
                    break;
                case "비":
                    snippetUi3.setImageResource(R.drawable.rain);
                    break;
                case "눈":
                    snippetUi3.setImageResource(R.drawable.snow);
                    break;
                case "눈/비":
                    snippetUi3.setImageResource(R.drawable.snow);
                    break;
            }

            TextView snippetUi4 = ((TextView) view.findViewById(R.id.riseTemp));
            snippetUi4.setTypeface(font);
            snippetUi4.setTextSize(20);
            snippetUi4.setTextColor(Color.parseColor("#834ca8"));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(c_rtemper);
                snippetUi4.setText(snippetText.subSequence(3,7)+"℃");
            } else {
                snippetUi4.setText("");
            }

            ImageView snippetUi5 = ((ImageView) view.findViewById(R.id.setCloud));
            switch (c_scloud){
                case "맑음":
                    snippetUi5.setImageResource(R.drawable.sun);
                    break;
                case "구름 조금":
                    snippetUi5.setImageResource(R.drawable.suncloud);
                    break;
                case "구름 많음":
                    snippetUi5.setImageResource(R.drawable.cloudy);
                    break;
                case "흐림":
                    snippetUi5.setImageResource(R.drawable.cloudy);
                    break;
                case "비":
                    snippetUi5.setImageResource(R.drawable.rain);
                    break;
                case "눈":
                    snippetUi5.setImageResource(R.drawable.snow);
                    break;
                case "눈/비":
                    snippetUi5.setImageResource(R.drawable.snow);
                    break;
            }

            TextView snippetUi6 = ((TextView) view.findViewById(R.id.setTemp));
            snippetUi6.setTypeface(font);
            snippetUi6.setTextSize(20);
            snippetUi6.setTextColor(Color.parseColor("#834ca8"));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(c_stemper);
                snippetUi6.setText(snippetText.subSequence(3,7)+"℃");
            } else {
                snippetUi6.setText("");
            }


        }
    }
}
