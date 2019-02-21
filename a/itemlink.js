var xItemIDs={};

var xBkmkIDs={};

var _extractItemID=function(sHref){

	//2013.8.8 Somehow CHM viewer inserts a slash into the item links;
	//It makes 'nyf://entry?itemid=' into this one: 'nyf://entry/?itemid=';

	var m=sHref.match(/^nyf:\/\/entry[\/]?\?itemid=(\d+)$/i);
	if(m && m.length>1){
		return m[1];
	}else{
		m=sHref.match(/^nyf:\/\/entry[\/]?\?bmid=(\d+)$/i);
		if(m && m.length>1){
			var sBmID=m[1];
			if(sBmID){
				return xBkmkIDs[sBmID];
			}
		}
	}
};

var _extractItemID_2=function(sHref){

	//2013.8.8 Somehow CHM viewer inserts a slash into the item links;
	//It makes 'nyf://entry?itemid=' into this one: 'nyf://entry/?itemid=';

	var sItemLinkHref='nyf://entry/?itemid=';
	var p=sHref.indexOf(sItemLinkHref);
	if(p==0){
		var sItemID=sHref.substr(p+sItemLinkHref.length);
		if(sItemID){
			return sItemID;
		}
	}

	sBkmkHref='nyf://entry/?bmid=';
	p=sHref.indexOf(sBkmkHref);
	if(p==0){
		var sBmID=sHref.substr(p+sBkmkHref.length);
		if(sBmID){
			return xBkmkIDs[sBmID];
		}
	}
};

var xAct=function(e){
	var xA=e.srcElement;
	var sHref=xA.href;
	if(sHref){
		var sID=_extractItemID(sHref);
		if(sID){
			var sFile=xItemIDs[sID];
			if(sFile){
				document.location.href=sFile;
				//e.cancelBubble=true;
				return false;
			}else{
				alert('Cannot load the target info item (#'+sID+')');
				//e.cancelBubble=true;
				return false;
			}
		}
	}
};

window.onload=function(){
	var v=document.getElementsByTagName('a');
	for(var i=0; i<v.length; ++i){
		var xA=v[i];
		if(xA){
			if(xA.addEventListener){
				xA.addEventListener('click', xAct, false);
			}else if(xA.attachEvent){
				xA.attachEvent('onclick', xAct);
			}
		}
	}
};
