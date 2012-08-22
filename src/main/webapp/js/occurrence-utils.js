//namespace regrouping utility functions
var canadensysUtils = (function(){
	//leftZeroPad for 2 digit integer only (day and month)
	//8 will return 08
	function dateElementZeroPad(intValue) {
		if(!intValue){
			return intValue;
		}
		return String("0" + intValue).slice(-2);
	}
	
	//Creates a string representation for the date in the format yyyy-mm-dd.
	//Month and day will be zero padded. Month and day are optional.
	//TODO Add validation, year and day only should not be valid
	function formatDate(year,month,day){
		var date = [];
		if(year){
			date.push(year);
		}
		if(month){
			date.push(dateElementZeroPad(month));
		}
		if(day){
			date.push(dateElementZeroPad(day));
		}
		return date.join('-');
	}
	
	//Tests if val is an Integer
	function isInteger(val){
		return !isNaN(parseInt(val));
	}
	
	function createCookie(name,value,days) {
		if(value.indexOf(';') >= 0){
			console.log('Invalid cookie value. The value must not contains ; character');
			return;
		}
		
	    if (days) {
	        var date = new Date();
	        date.setTime(date.getTime()+(days*24*60*60*1000));
	        var expires = "; expires="+date.toGMTString();
	    }
	    else var expires = "";
	    document.cookie = name+"="+value+expires+"; path=/";
	}

	function readCookie(name) {
	    var nameEQ = name + "=";
	    var ca = document.cookie.split(';');
	    for(var i=0;i < ca.length;i++) {
	        var c = ca[i];
	        while (c.charAt(0)==' ') c = c.substring(1,c.length);
	        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	    }
	    return null;
	}

	function eraseCookie(name) {
	    createCookie(name,"",-1);
	}
	
	//Public methods
    return {
    	dateElementZeroPad : dateElementZeroPad,
    	formatDate : formatDate,
    	isInteger : isInteger,
    	createCookie : createCookie,
    	readCookie : readCookie
	};
}());