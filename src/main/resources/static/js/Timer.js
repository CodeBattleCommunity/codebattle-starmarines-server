/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var timer;
var nextGameTime;
var text;
var container;

function getNextGameTime(){
    var xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }



    var request = "./settings.html?type=NEXT_GAME_TIME";
    xmlhttp.open("GET", request, true);
   
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
           
            nextGameTime = new Date(JSON.parse(xmlhttp.responseText).NextGameTime);
            
            var timer = setInterval(countDown,100);
        }
    }
    xmlhttp.send();
}

function initTimer() {

    container = $('#countdown');
    text = container.text();
    getNextGameTime();
  
}


function getWord(digit, words) {//words[] = день, Дня, Дней
    var lastFigure = digit % 10;
    if (digit > 11 && digit < 15)
    {
        return words[2];
    }
    else
    {
        if (lastFigure == 1) return words[0];
        if (lastFigure > 1 && lastFigure < 5) return words[1];
        if (lastFigure == 0 || lastFigure >= 5) return words[2];
    }
}

function getDay(number) {
    return getWord(number,['день','дня\u00A0','дней']);
}


function getHour(number) {
    return getWord(number,['час\u00A0\u0013','часа\u00A0','часов']);
}

function getMinute(number) {
    return getWord(number,['минута','минуты','минут\u00A0']);
}

function getSecond(number) {
    return getWord(number,['секунда','секунды','секунд\u00A0']);
}

function trimToSize(number) {
    return number.toString().length>1 ?  number.toString() :  '0' + number.toString();
}

function chooseTimer(timeDiff) {
    var seconds = Math.floor(timeDiff / 1000);
    var minutes = Math.floor(seconds / 60);
    var hours = Math.floor(minutes / 60);
    var days = Math.floor(hours / 24);
    hours = hours % 24;
    minutes = minutes % 60;
    seconds = seconds % 60;
    timeDiff = timeDiff % 100;
    if( days    > 0 ) return days + ' ' + getDay(days);
    if( hours   > 0 ) return hours + ' ' + getHour(hours);
    text = 'Турнир начнется';
    return 'с минуты на минуту';

        //До турнира осталось 20 дней.
        //До турнира осталось 5 часов.
        //Турнир начнется с минуты на минуту.

    /*' '
            + days  + ' ' + getDay(days) + ' '
            + hours + ' ' + getHour(hours) + ' '
            + minutes + ' ' +  getMinute(minutes) + ' '
            +  seconds + ' ' +  getSecond(seconds);
*/


}

function countDown() {


    var now = new Date();
    var timeDiff = nextGameTime.getTime() - now.getTime();
    //    console.log(timeDiff);
    if(timeDiff <= 0) {
        $('#countdown').text('К бою!')
    } else {
        var message = chooseTimer(timeDiff);
      
        $('#countdown').text(text + ' ' + message);
    }
}


