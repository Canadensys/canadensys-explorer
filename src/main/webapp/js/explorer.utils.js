/****************************
Copyright (c) 2013 Canadensys
Explorer Utilities
****************************/
/*global EXPLORER, $, window, document, console, google*/

EXPLORER.utils = (function(){

  'use strict';

  var _private = {

    //leftZeroPad for 2 digit integer only (day and month)
    //8 will return 08
    dateElementZeroPad: function(intValue) {
      if(!intValue){
        return intValue;
      }
      return String("0" + intValue).slice(-2);
    },

    //Creates a string representation for the date in the format yyyy-mm-dd.
    //Month and day will be zero padded. Month and day are optional.
    formatDate: function(year,month,day){
      var date = [];
      if(year){
        date.push(year);
      }
      if(month){
        date.push(this.dateElementZeroPad(month));
      }
      if(day){
        date.push(this.dateElementZeroPad(day));
      }
      return date.join('-');
    },

    //Tests if val is an Integer
    isInteger: function(val){
      return !isNaN(parseInt(val, 10));
    },

    getParameterByName: function(name) {
      var cname   = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]"),
          regexS  = "[\\?&]" + cname + "=([^&#]*)",
          regex   = new RegExp(regexS),
          results = regex.exec(window.location.href);

      if(results === null) { return ""; }
      return decodeURIComponent(results[1].replace(/\+/g, " "));
    },

    isWithinYearLimit: function(year){
      return (year >0 && year < 9999);
    },

    isWithinMonthLimit: function(month){
      return (month >=1 && month <=12);
    },

    isWithinDayLimit: function(day){
      return (day >=1 && day <=31);
    },

    isValidNumber: function(number){
      if(isNaN(number)){
        return false;
      }
      return true;
    },

    isValidEmail: function(email){
      return (/^\S+@\S+\.\S+$/i).test(email);
    },

    isValidDateElement: function($el){
      var val = $el.val();
      //set the base of parseInt to 10 to accept int like 08
      var valAsInt = parseInt(val,10);
      //make sure it's a number
      if(isNaN(valAsInt)){
        return false;
      }

      if($el.hasClass('validationYear')){
        return this.isWithinYearLimit(valAsInt);
      }
      if($el.hasClass('validationMonth')){
        return this.isWithinMonthLimit(valAsInt);
      }
      if($el.hasClass('validationDay')){
        return this.isWithinDayLimit(valAsInt);
      }
    },

    isValidPartialDate: function(year,month,day){
      //set the base of parseInt to 10 to accept int like 08
      var yAsInt = parseInt(year,10),
          mAsInt = parseInt(month,10),
          dAsInt = parseInt(day,10);

      //accept all partial dates for year alone and year/month if the day is not specified
      if(!isNaN(yAsInt) && isNaN(dAsInt)){
        return this.isWithinYearLimit(yAsInt) && (isNaN(mAsInt) || this.isWithinMonthLimit(mAsInt));
      }

      //accept all partial dates for month/day
      if(isNaN(yAsInt) && !isNaN(mAsInt) && !isNaN(dAsInt)){
        return this.isWithinMonthLimit(mAsInt) && this.isWithinDayLimit(dAsInt);
      }

      //accept all partial dates for month alone
      if(isNaN(yAsInt) && !isNaN(mAsInt) && isNaN(dAsInt)){
        return this.isWithinMonthLimit(mAsInt);
      }

      //accept all partial dates for day alone
      if(isNaN(yAsInt) && isNaN(mAsInt) && !isNaN(dAsInt)){
        return this.isWithinDayLimit(dAsInt);
      }

      if(!isNaN(yAsInt) && !isNaN(mAsInt) && !isNaN(dAsInt)){
        // day 0 means the last day/hour of the previous month, Date is 0 based so we ask for the month later
        var date = new Date(yAsInt, mAsInt,0);
        if(dAsInt > date.getDate()){
          return false;
        }
        return this.isWithinYearLimit(yAsInt) && this.isWithinMonthLimit(mAsInt) && this.isWithinDayLimit(dAsInt);
      }
      return false;
    },

    isValidDateInterval: function(syear,smonth,sday,eyear,emonth,eday){
      return (isNaN(parseInt(syear, 10)) === isNaN(parseInt(eyear, 10)) &&
          isNaN(parseInt(smonth, 10)) === isNaN(parseInt(emonth, 10)) &&
          isNaN(parseInt(sday, 10)) === isNaN(parseInt(eday, 10)));
    },
    
    isScrolledIntoView: function($el) {
      var docViewTop = $(window).scrollTop(),
          docViewBottom = docViewTop + $(window).height(),
          elemTop = $el.offset().top,
          elemBottom = elemTop + $el.height();

      return ((elemBottom >= docViewTop) && (elemTop <= docViewBottom)
        && (elemBottom <= docViewBottom) &&  (elemTop >= docViewTop) );
    }

  };
  
  return {
    init: function() { return; },
    dateElementZeroPad: function(intValue) {
      return _private.dateElementZeroPad(intValue);
    },
    formatDate: function(year,month,day) {
      return _private.formatDate(year,month,day);
    },
    isInteger: function(val) {
      return _private.isInteger(val);
    },
    getParameterByName: function(name) {
      return _private.getParameterByName(name);
    },
    isWithinYearLimit: function(year) {
      return _private.isWithinYearLimit(year);
    },
    isWithinMonthLimit: function(month) {
      return _private.isWithinMonthLimit(month);
    },
    isWithinDayLimit: function(day) {
      return _private.isWithinDayLimit(day);
    },
    isValidNumber: function(number) {
      return _private.isValidNumber(number);
    },
    isValidEmail: function(email) {
      return _private.isValidEmail(email);
    },
    isValidDateElement: function(el) {
      return _private.isValidDateElement(el);
    },
    isValidPartialDate: function(year,month,day) {
      return _private.isValidPartialDate(year,month,day);
    },
    isValidDateInterval: function(syear,smonth,sday,eyear,emonth,eday) {
      return _private.isValidDateInterval(syear,smonth,sday,eyear,emonth,eday);
    },
    isScrolledIntoView: function(element) {
      return _private.isScrolledIntoView(element);
    }
  };

}());