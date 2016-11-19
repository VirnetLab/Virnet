/**
 * change url hash and add event echo  
 */

window.addEventListener('hashchange', function(e){
  if (history.state){
    var state = history.state;
    //do something(state.url, state.title);
    switch(state.funName){
    case 'fetchData' : var data = state.data; fetchData(data['id'], data['user'], data['page'], data['select']);
    }
  }
});

function storeHistory(hash, funName, data){
	url = window.location.href;
	Url = url.split('#');
	
	var state = {
		    title : "",
		    url : Url[0] + "#" + hash,
		    funName : funName,
		    data : data
		};
	window.history.pushState(state, document.title, Url[0] + "#" + hash);
}