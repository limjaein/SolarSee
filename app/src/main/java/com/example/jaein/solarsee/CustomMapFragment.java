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


public class CustomMapFragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapClickListener{


    GoogleMap map;
    private MapView mapView;
    LatLng Center;
    ArrayList<PlaceInfo> m_data;
    Date date;
    String curDate;
    String sunriseInfo, sunsetInfo;
    SimpleDateFormat CurDateFormat;

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
        date = new Date(System.currentTimeMillis());
        curDate = CurDateFormat.format(date);   //현재날짜

        Center = new LatLng(36.573497, 127.359965);
        m_data = new ArrayList<>();
        m_data.add(new PlaceInfo(new LatLng(37.723669, 129.029123),"정동진"));
        m_data.add(new PlaceInfo(new LatLng(35.359157, 129.360375),"간절곶"));
        m_data.add(new PlaceInfo(new LatLng(35.117180, 129.121676),"이기대"));
        m_data.add(new PlaceInfo(new LatLng(33.458351, 126.942457),"성산일출봉"));
        m_data.add(new PlaceInfo(new LatLng(36.267594, 129.607168),"호미곶"));
        m_data.add(new PlaceInfo(new LatLng(37.792205, 126.841448),"하늘공원"));
        m_data.add(new PlaceInfo(new LatLng(36.502667, 126.337039),"꽃지해안공원"));
        m_data.add(new PlaceInfo(new LatLng(35.684325, 126.531795),"변산반도"));
        m_data.add(new PlaceInfo(new LatLng(34.303308, 126.527242),"땅끝마을"));
        m_data.add(new PlaceInfo(new LatLng(34.593588, 127.802856),"향일암"));
        m_data.add(new PlaceInfo(new LatLng(35.338586, 127.731338),"지리산천왕봉"));
        m_data.add(new PlaceInfo(new LatLng(37.098003, 128.915235),"태백산"));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMapClickListener(this);

        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);


        for(int i=0; i<m_data.size(); i++){
            LatLng latLng = m_data.get(i).getP_latlng();
            parsingTime(latLng, curDate, m_data.get(i));
        }

        updateMap();
    }

    public void updateMap(){
        map.moveCamera(CameraUpdateFactory.newLatLng(Center));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Center, 6.5f));
    }
    public void parsingTime(LatLng latlng, final String date, final PlaceInfo data){
        double par_lat = latlng.latitude;
        double par_log = latlng.longitude;
        Ion.with(this)
                .load("http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo?longitude="+par_log+
                        "&latitude="+par_lat+"&locdate="+date+"&dnYn=Y&" +
                        "ServiceKey=WKilB3x6pMR6xXG5MBPsHLE3Xxq48pi1S02fQjef%2FY83AadtPrPr9Wq1mhzsOsJ2efkrLD0i4KW8irbmFnhq0w%3D%3D")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try{
                            parsingXML(result);
                            data.setInfo(sunriseInfo, sunsetInfo);
                            Marker mk = map.addMarker(new MarkerOptions()
                            .position(data.getP_latlng())
                            .title(data.getP_placeName())
                            .snippet(data.getP_sunRise()+","+data.getP_sunSet()));

                            map.setInfoWindowAdapter(new CustomInfoWindowAdapter());

//                            MarkerOptions marker = new MarkerOptions();
//                            marker.position(data.getP_latlng());
//                            marker.title(data.getP_placeName());
//                            marker.snippet(data.getP_sunRise()+"\n"+data.getP_sunSet());
//                            map.addMarker(marker);
                            //Toast.makeText(getActivity(), data.getP_placeName(), Toast.LENGTH_SHORT).show();
                        }catch (Exception E){
                            e.printStackTrace();
                        }
                    }
                });
        //Toast.makeText(getActivity(), info[0].getSunRise(), Toast.LENGTH_SHORT).show();
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
                        sunriseInfo = "일출시간"+":";
                        bSet = 1;
                    }
                    else if(tag_name.equals("sunset") ){
                        sunsetInfo = "일몰시간"+":";
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
                        sunriseInfo += hour+"시"+min+"분"+sec+"초";
                        //Toast.makeText(getActivity(), sunriseInfo, Toast.LENGTH_SHORT).show();
                        bSet = 0;
                    }
                    else if(bSet==2){
                        String hour = xpo.getText().substring(0,2);
                        String min = xpo.getText().substring(2,4);
                        String sec = xpo.getText().substring(4,6);
                        sunsetInfo += hour+"시"+min+"분"+sec+"초";
                        //Toast.makeText(getActivity(), sunsetInfo, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onMapClick(LatLng latLng) {

    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getActivity().getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
//                // This means that getInfoContents will be called.
//                return null;
//            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//                // This means that the default info contents will be used.
//                return null;
//            }
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {

            LinearLayout li = (LinearLayout)view.findViewById(R.id.linear);
            li.setBackgroundResource(R.drawable.custom_info_bubble);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            StringTokenizer tokens = new StringTokenizer(snippet);
            String sunRise = tokens.nextToken(",");
            String sunSet = tokens.nextToken(",");

            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet1));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(sunRise);
//                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
//                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 14, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }

            TextView snippetUi2 = ((TextView) view.findViewById(R.id.snippet2));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(sunSet);
//                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
//                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi2.setText(snippetText);
            } else {
                snippetUi2.setText("");
            }
        }
    }
}
