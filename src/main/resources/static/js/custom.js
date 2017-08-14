var $loading = $('#loading').hide();
$(function(){
		
	
	$(".deleteCard").click(function(){
		var name=$(this).attr("name");
		var id=$(this).attr("id");
		if(name !='undefined'){
			
			var modal=$("#myModal");
			modal.find(".modal-body").html('<p class="delelableEvent" id="'+id+'">'+name+'</p>'			
			);
			modal.modal("show");
		}
	});
	
	$(".confirm2delete").click(function(){
		
		var id=$(".delelableEvent").attr("id");
		if(id != 'undefined'){		
			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
					
			$.ajax({
			    url: '/admin/events/'+id,
			    type: 'DELETE',
			    beforeSend: function(request) {
			        request.setRequestHeader(header, token);
			      },			    
			    success: function(result) {
			       console.log("Deleted successfully");
			       window.top.location.reload();
			    },
			    error:function(result){
			    	console.log("Error in deletion");
				    window.top.location.reload();
			    }
			      
			});
		}		
	});	
	
	
	//To ellipsis the text of event short description
	$(".shortDescEllipsis").dotdotdot();	
		

	$('.showSpinner').on('submit', function(e) { //use on if jQuery 1.7+
	  $('#loading').show();
    });
	
	$('.showSpinner').click(function(e) { //use on if jQuery 1.7+
		  $('#loading').show();
	    });
			
	
}).ajaxStart(function () {	
	console.log("loading start");
    $('#loading').show();  // show loading indicator
}).ajaxStop(function() 
		{
	console.log("loading end");
    $('#loading').hide();  // hide loading indicator
}).ajaxError(function(){
	console.log("loading end");
    $('#loading').hide();  // hide loading indicator
});
