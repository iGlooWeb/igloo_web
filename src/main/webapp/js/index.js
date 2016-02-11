/**
 * @author Yikai Gong
 */
var map;
var markers = [];
var devices = [];
var urlPrefix = "http://"+ window.location.host + window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/'));
var pickedMarker = {};


function index_main(){
    drawMap(city_geo.Melbourne.center);
    drawStateChart();
    requestDevicesInfo(city_geo.Melbourne.boundingBox);
    initInputGroup();
}

function drawMap(coordinates){
    var mapOptions = {
        zoom: 9,
        center: { lat: coordinates.latitude, lng: coordinates.longitude},
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
}

function requestDevicesInfo(json){
    $.ajax({
        url: urlPrefix +'/api/devices/all',
        data: json,
        type: 'get',
        dataType: 'json',
        success: function(data) {
            deleteMarkders();
            for(var i in data){
                var deviceInfo = data[i];
                var latitude = deviceInfo[1];
                var longitude = deviceInfo[2];
                var options = {
                    position: new google.maps.LatLng(latitude, longitude),
                    icon: circleRed
                };
                var marker = new google.maps.Marker(options);
                addListenerToMarker(marker, deviceInfo, map);
                markers.push(marker);
            }
            updateMarkers();
            drawDevicesPieChart(data);
            updateCityInfo(data);
        },
        error : function(e){
            console.log(e);
        }
    });
}

function deleteMarkders(){
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

function addListenerToMarker(marker, deviceInfo, map){
    google.maps.event.addListener(marker, 'click', function(){
        var chip_id = deviceInfo[0];
        $.ajax({
            url: urlPrefix + "/api/recordsByChip/" + chip_id,
            type: 'get',
            dataType: 'json',
            success: function(data) {
                console.log(data);
                // update map view
                var last_record = data[0];
//                console.log(last_record.dealer);
                var content =
                    "Zone ID: " + last_record.zone_id + '</br>' +
                    "Chip ID: " + last_record.chip_id + '</br>' +
                    "Model: " + last_record.model + '</br>' +
//                    "Device ID: " + last_record.serial_number + '</br>' +
                    "Heater: " + last_record.heater_name + '</br>' +
                    "Dealer: " + last_record.dealer + '</br>' +
                    "Suburb: " + last_record.suburb + '</br>' +
                    "Installed Date: " + last_record.installed_date + '</br>' +
//                    "Last Update Time: " + convertDateFormat2(last_record.timestamp) +'</br>' +
                    " <button type=\"button\" class=\"btn btn-xs btn-primary\" id=\"requestBtm\" onclick='requestBtmHandler("+ last_record.serial_number +");'>Request</button>"+
                    "&nbsp&nbsp&nbsp&nbsp"+
                    " <button type=\"button\" class=\"btn btn-xs btn-danger\" id=\"deleteBtm\" onclick='deleteBtmHandler(\""+ last_record.chip_id +"\");'>Delete</button>";
                marker.infoWindow = new google.maps.InfoWindow({content:content, title:"Device Info"});
                if (pickedMarker.infoWindow){
                    pickedMarker.infoWindow.close();
                    pickedMarker.setIcon(circleRed);
                    pickedMarker.selected = false;
                }
                marker.infoWindow.open(map, marker);
                pickedMarker = marker;
                pickedMarker.selected = true;
                pickedMarker.setIcon(circleGreen2);

                // update charts and tables
                drawDailyHOOChart(chip_id, getStartDateStr(13) ,13);
                drawMonthlyHOOChart(chip_id, new Date().getFullYear());
                drawStateChart(data);

                //end of a query execution
//                google.maps.event.addListener(marker.infoWindow, 'domready', function() {
//                    document.id("requestBtm").addEvent("submit", function(e) {
////                        e.stop();
//                        console.log("hi!");
//                    });
//                });
            },
            error : function(e){
                console.log(e);
            }
        });
    });

    // icon color switch
    google.maps.event.addListener(marker, "mouseover", function (e) { marker.setIcon(circleBlue2); });
    google.maps.event.addListener(marker, "mouseout", function (e) {
        if(marker.selected || marker.selected == true)
            marker.setIcon(circleGreen2);
        else
            marker.setIcon(circleRed);
    });
}

function updateMarkers(){
    for(var i in markers){
        markers[i].setMap(map);
        console.log(markers[i])
    }
}

function drawStateChart(data){
    var tableData=[];
    for(var i in data){
        var record = data[i];
        var row = [];
        row.push(convertDateFormat(record.timestamp));
        row.push(record.pwo == 0? 'Off':'On');
//        row.push(record.tem+" "+'&#176;C');
        row.push(record.thm == 0? 'Off':'On');
        row.push(record.flm);
        row.push(record.fan);
        row.push(record.au1);
        row.push(record.au2);
//        row.push(record.cld == 0? 'Off':'On');
        tableData.push(row);
    }
    $('#state_table').dataTable({
        "data": tableData,
        "oLanguage": {
            "sEmptyTable": "Please select a point/device on the map above first."
        },
        "columns":[
            {"title": "Time Stamp"},
            {"title": "Power"},
//            {"title": "Temperature"},
            {"title": "Thermostat"},
            {"title": "Flame Level"},
            {"title": "Fan Level"},
            {"title": "Aux1"},
            {"title": "Aux2"}
//            {"title": "Child Lock Version"}
        ],
        "bDestroy" : true,
        "aLengthMenu": [[5, 10, 20], [5, 10, 20]],
        "iDisplayLength": 5,
        "order": [[ 0, "desc" ]]
    });
}


function drawMonthlyHOOChart(chip_id, year){
    $.ajax({
        url: urlPrefix +'/api/records/operationHourPerMonth/'+chip_id+"/"+year,
        type: 'get',
        dataType: 'json',
        success: function(data) {
            console.log(data);
            var serialData = [0,0,0,0,0,0,0,0,0,0,0,0]
            for(var i in data){
                var month = data[i].month;
                var hours = data[i].hours;
                serialData[month-1] = hours;
            }
            $('#operationHour_perMonth').highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: null
                },
                legend: {
                    enabled: true
                },
                xAxis: {
                    categories: [
                        'Jan',
                        'Feb',
                        'Mar',
                        'Apr',
                        'May',
                        'Jun',
                        'Jul',
                        'Aug',
                        'Sep',
                        'Oct',
                        'Nov',
                        'Dec'
                    ]
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Operation Hours'
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
                    pointFormat:
                        '<td style="padding:0; font-size:11px"><b>{point.y:.1f} hours</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                credits: {
                    enabled: false
                },
                series: [{
                    name: "Device(chip): " + chip_id,
                    data: serialData
                }]
            });
        },
        error : function(e){
            console.log(e);
        }
    });
}

function drawDailyHOOChart(chip_id, startDateStr, days){
    $.ajax({
        url: urlPrefix +'/api/records/operationHour/'+chip_id+"/"+startDateStr,
        type: 'get',
        dataType: 'json',
        success: function(data) {
            console.log(data);
            var xData = [];
            var yData = [];
            for (var i = 0; i<days+1; i++){
                var date = getStartDateStr(days - i);
                xData.push(convertDateFormat(date));
                yData.push(0);
                for (var j in data){
                    if(isDateMatch(data[j].date, date)){
                        yData[i] += data[j].hours;
                    }
                }
            }
            $('#operationHour_perDay').highcharts({
                chart: {
                    type: 'line'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: null
                },
//                legend: {
//                    enable: false
//                },
                xAxis: {
                    categories: xData,
                    labels: {
                        rotation: -45,
                        style: {
                            fontSize: '13px',
                            fontFamily: 'Verdana, sans-serif'
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: 'Operation Hours'
                    }
                },
                plotOptions: {
                    line: {
                        dataLabels: {
                            enabled: true
                        },
                        enableMouseTracking: false
                    }
                },
                credits: {
                    enabled: false
                },
                series: [{
                    name: 'Device: '+ chip_id,
                    data: yData
                }]
            });
        },
        error : function(e){
            console.log(e);
        }
    });
}

function initInputGroup(){
    initCitySelection();
}

function initCitySelection(){
    document.getElementById("city_selection").addEventListener("change", function(event){
        var city = event.target.value;
        refreshCity(city);
    });
}

function updateCityInfo(data){
    document.getElementById("number_of_devices").innerHTML = data.length;
}

function drawDevicesPieChart(data){
    console.log(data);
    var output = [];
    for(var i in data){
        var deviceInfo = data[i];
        var suburb = deviceInfo[3];
        accumulate(output, suburb);
    }
//    console.log(output);
    $('#devices_pie_chart').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: 1,//null,
            plotShadow: false
        },
        title: {
            text: null
        },
        tooltip: {
            pointFormat: '{series.name}: {point.y:.0f} (<b>{point.percentage:.1f}%</b>)'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                }
            }
        },
        credits: {
            enabled: false
        },
        series: [{
            type: 'pie',
            name: 'Num of devices',
            data: output
        }]
    });
}

var city_geo = {
//    Melbourne: {boundingBox: {latiMin: -38.554744, latiMax: -37.516398, lonMin: 144.091187, lonMax: 145.739136}, center: {latitude: -37.814107, longitude: 144.963280}},
//    Sydney: {boundingBox: {latiMin: -34.292467, latiMax: -33.490480, lonMin: 150.394592, lonMax: 151.662140}, center: {latitude: -33.838215, longitude: 151.101151}},
//    Seattle: {boundingBox: {latiMin: 46.988625, latiMax: 48.031207, lonMin: -122.862854, lonMax: -121.621399}, center: {latitude: 47.606209, longitude: -122.332071}},
//    Los_Angeles: {boundingBox: {latiMin: 33.203001, latiMax: 34.403440, lonMin: -119.347229, lonMax: -116.545715}, center: {latitude: 34.039108, longitude: -118.280899}}

    Melbourne: {boundingBox: {latiMin: -90, latiMax: 90, lonMin: -180, lonMax: 180}, center: {latitude: -37.814107, longitude: 144.963280}},
    Sydney: {boundingBox: {latiMin: -90, latiMax: 90, lonMin: -180, lonMax: 180}, center: {latitude: -33.838215, longitude: 151.101151}},
    Seattle: {boundingBox: {latiMin: -90, latiMax: 90, lonMin: -180, lonMax: 180}, center: {latitude: 47.606209, longitude: -122.332071}},
    Los_Angeles: {boundingBox: {latiMin: -90, latiMax: 90, lonMin: -180, lonMax: 180}, center: {latitude: 34.039108, longitude: -118.280899}},
    Minnesota: {boundingBox: {latiMin: -90, latiMax: 90, lonMin: -180, lonMax: 180}, center: {latitude: 46.729553, longitude: -94.685900}},
    New_Zealand: {boundingBox: {latiMin: -90, latiMax: 90, lonMin: -180, lonMax: 180}, center: {latitude: -40.900557, longitude: 174.885971}}

}

var circleRed ={
    path: google.maps.SymbolPath.CIRCLE,
    fillColor: 'crimson',
    fillOpacity: 0.9,
    scale: 3,
    strokeColor: 'crimson',
    strokeWeight: 1
};

var circleBlue2 ={
    path: google.maps.SymbolPath.CIRCLE,
    fillColor: 'blue',
    fillOpacity: 1.2,
    scale: 6.5,
    strokeColor: 'blue',
    strokeWeight: 1
};

var circleGreen2 ={
    path: google.maps.SymbolPath.CIRCLE,
    fillColor: 'green',
    fillOpacity: 1.2,
    scale: 6.5,
    strokeColor: 'blue',
    strokeWeight: 1
};

function accumulate(array, value){
    for(var i in array){
        if(array[i][0]==value){
            array[i][1]++;
            return;
        }
    }
    array.push([value, 1]);
}

function convertDateFormat(timeStampStr){
    var timeStampArray = timeStampStr.split(' ');
    var dateComponents = timeStampArray[0].split('-');
    if(timeStampArray[1] != undefined)
        return dateComponents[2] + '-' +  dateComponents[1] + '-' + dateComponents[0] + ' ' + timeStampArray[1];
    else
        return dateComponents[2] + '-' +  dateComponents[1] + '-' + dateComponents[0];
}

function convertDateFormat2(timeStampStr){
    var timeStampArray = timeStampStr.split(' ');
    var dateComponents = timeStampArray[0].split('-');
    if(timeStampArray[1] != undefined)
        return dateComponents[2] + '-' +  dateComponents[1] + '-' + dateComponents[0] + ' ' + convertTimeFormat(timeStampArray[1]);
    else
        return dateComponents[2] + '-' +  dateComponents[1] + '-' + dateComponents[0];
}

function convertTimeFormat(time){
    var timeComponents = time.split(":");
    var hour = parseInt(timeComponents[0]);
    var min = timeComponents[1];
    var sec = timeComponents[2];
    if(hour>12){
        hour -= 12;
        return hour + ":" + min + ":" + sec +" PM";
    }
    else{
        return hour + ":" + min + ":" + sec +" AM";
    }
}

function getStartDateStr(numOfDaysBefore){
    var date = new Date();
    date.setHours(0);
    date.setMinutes(0,0,0);
    var datenum = Date.parse(date);
    datenum -= numOfDaysBefore * 24 * 60 * 60 *1000;
    date = new Date(datenum);
    return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
}

function isDateMatch(date1, date2){
    var date1_comp = date1.split("-");
    var year1 = parseInt(date1_comp[0]);
    var month1 = parseInt(date1_comp[1]);
    var day1 = parseInt(date1_comp[2]);

    var date2_comp = date2.split("-");
    var year2 = parseInt(date2_comp[0]);
    var month2 = parseInt(date2_comp[1]);
    var day2 = parseInt(date2_comp[2]);

    if(year1==year2 && month1==month2 && day1==day2)
        return true;
    else
        return false
}

function deleteBtmHandler(chip_id){
    var confirmed = confirm("Are you sure to delete all the data related to Chip ID: "+ chip_id);
    if (confirmed == true){
        console.log("delete");
        $.ajax({
            url: urlPrefix +'/api/recordsByChip/'+chip_id,
            type: 'delete',
            dataType: 'json',
            success: function(data) {
                console.log(data);
                var city = document.getElementById("city_selection").value;
                requestDevicesInfo(city_geo[city]);
            },
            error : function(e){
                console.log(e);
            }
        });
    }
}

function refreshCity(city){
    switch(city){
        case 'Melbourne':
            map.setCenter({lat: city_geo.Melbourne.center.latitude, lng: city_geo.Melbourne.center.longitude});
            map.setZoom(9);
            requestDevicesInfo(city_geo.Melbourne.boundingBox);
            break;
        case 'Sydney':
            map.setCenter({lat: city_geo.Sydney.center.latitude, lng: city_geo.Sydney.center.longitude});
            map.setZoom(9);
            requestDevicesInfo(city_geo.Sydney.boundingBox);
            break;
        case 'Seattle':
            map.setCenter({lat: city_geo.Seattle.center.latitude, lng: city_geo.Seattle.center.longitude});
            map.setZoom(9);
            requestDevicesInfo(city_geo.Seattle.boundingBox);
            break;
        case 'Los_Angeles':
            map.setCenter({lat: city_geo.Los_Angeles.center.latitude, lng: city_geo.Los_Angeles.center.longitude});
            map.setZoom(9);
            requestDevicesInfo(city_geo.Los_Angeles.boundingBox);
            break;
        case 'Minnesota':
            map.setCenter({lat: city_geo.Minnesota.center.latitude, lng: city_geo.Minnesota.center.longitude});
            map.setZoom(6);
            requestDevicesInfo(city_geo.Minnesota.boundingBox);
            break;
        case 'New_Zealand':
            map.setCenter({lat: city_geo.New_Zealand.center.latitude, lng: city_geo.New_Zealand.center.longitude});
            map.setZoom(6);
            requestDevicesInfo(city_geo.New_Zealand.boundingBox);
            break;
    }
}