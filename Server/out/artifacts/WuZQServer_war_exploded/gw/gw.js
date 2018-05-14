/**
 * Created by wangzd on 2018/1/17.
 */
const mapstyles=
    't%3Aroad%7Ce%3Aall%7Cv%3Aoff%2Ct%3Awater%7Ce%3Aall%7Cc%3A%23021019%2Ct%3Ahighway%7Ce%3Ag.f%7Cv%3Aoff%7Cc%3A%23000000%2Ct%3Ahighway%7Ce%3Ag.s%7Cv%3Aoff%7Cc%3A%23147a92%2Ct%3Aarterial%7Ce%3Ag.f%7Cc%3A%23000000%2Ct%3Aarterial%7Ce%3Ag.s%7Cc%3A%230b3d51%2Ct%3Alocal%7Ce%3Ag%7Cc%3A%23000000%2Ct%3Aland%7Ce%3Aall%7Cc%3A%2308304b%2Ct%3Arailway%7Ce%3Ag.f%7Cc%3A%23000000%2Ct%3Arailway%7Ce%3Ag.s%7Cc%3A%2308304b%2Ct%3Asubway%7Ce%3Ag%7Cl%3A-70%2Ct%3Abuilding%7Ce%3Ag.f%7Cc%3A%23000000%2Ct%3Aall%7Ce%3Al.t.f%7Cc%3A%23857f7f%2Ct%3Aall%7Ce%3Al.t.s%7Cc%3A%23000000%2Ct%3Abuilding%7Ce%3Ag%7Cc%3A%23022338%2Ct%3Agreen%7Ce%3Ag%7Cc%3A%23062032%2Ct%3Aboundary%7Ce%3Aall%7Cc%3A%231e1c1c%2Ct%3Amanmade%7Ce%3Aall%7Cc%3A%23022338';
var x_PI = 3.14159265358979324 * 3000.0 / 180.0;
var PI = 3.1415926535897932384626;
var a = 6378245.0;
var ee = 0.00669342162296594323;
var app = angular.module('myApp', []);
app.controller("main",function($scope){
        console.info('aaaaaa');
        $scope.mm=4;
    //var map= L.map('map',{ center: [30.77,120.76],zoom:16,crs: window.L.CRS.EPSGB3857,});
    //new window.L.TileLayer.BaiduLayer("Normal.Map",{styles:mapstyles}).addTo(map);
    var map= L.map('map',{ center: [30.77,120.76],zoom:16});
    new window.L.tileLayer.chinaProvider('GaoDe.Normal.Map',{
        maxZoom: 18,
        minZoom: 5
    }).addTo(map);
    function wgs84togcj02(node) {
            var lng=node.lng;
                var lat=node.lat;
            var dlat = transformlat(lng - 105.0, lat - 35.0);
            var dlng = transformlng(lng - 105.0, lat - 35.0);
            var radlat = lat / 180.0 * PI;
            var magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            var sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            var mglat = lat + dlat;
            var mglng = lng + dlng;
        node.lng=mglng;
        node.lat=mglat;
            return node;

    }
    function transformlat(lng, lat) {
        var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret
    }

    function transformlng(lng, lat) {
        var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret
    }
    function gcj02tobd09(node) {
        var lng=node.lng;
        var lat=node.lat;
        var z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
        var theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
        var bd_lng = z * Math.cos(theta) + 0.0065;
        var bd_lat = z * Math.sin(theta) + 0.006;
        node.lng=bd_lng;
        node.lat=bd_lat;
        return node;
    }

    res.map(function(item){

        wgs84togcj02(item.node);
        //gcj02tobd09(item.node)
        if(item.links!=null&&item.links.length>0)
        item.links.map(function(item){
            wgs84togcj02(item);
            //gcj02tobd09(item)
        })
    });


    for ( var i=0; i < res.length; ++i )
    {
        L.circle([res[i].node.lat, res[i].node.lng], 0.4,{color:'red', stroke:true, fillOpacity:0.8}).addTo(map);


        if(res[i].links!=null){

            for( var j=0;j <res[i].links.length;j++ ){


                var polylinePoints = [
                    new L.LatLng(res[i].node.lat, res[i].node.lng),new L.LatLng(res[i].links[j].lat, res[i].links[j].lng),];


                var polylineOptions = {
                    color: 'red',
                    weight: 1,
                    opacity: 0.9

                };


                var polyline = new L.Polyline(polylinePoints, polylineOptions);


                map.addLayer(polyline);
            }

        }

    }
})
