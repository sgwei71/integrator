var simulatormain = {
		init : function() {
			var _this = this;
			
			//$('#destinationIp').val('http://10.104.162.77/nomal/test');
			//$('#data').text('{"sttlLen" : "6", "sttlCmrsYn" : "Y", "sttlEncpDcd" : "I", "sttlLnggDcd" : "CO", "sttlVerDsnc" : "IOC", "sttlWrtnYmd" : "20180322", "sttl_adapter_id" : ""}');
			
			$('#destinationIp').val('http://10.104.162.77:18181/httpin');
			
			$('#btnSendHttp').on('click', function () {
				
				if ($('#destinationIp').val() == '') {
					alert('IP를 입력해주세요.');
				}
				else if ($('#data').val() == '') {
					alert('데이터를 입력해주세요.');
				}
				else {
					var obj = new Object();
					obj.destinationIp = $('#destinationIp').val();
					obj.data = $('#data').val();
					
					_this.sendhttp(obj);
				}
			});
		},
		sendhttp : function(info) {
			$.ajax({
				type : 'POST',
				url  : 'http://10.104.162.77:18181/httpin',
				//dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				data: JSON.stringify(info)
			}).done(function(result) {
				$('#resultContent').text(result);
			}).fail(function (error) {
				$('#resultContent').text("에러 내용 : " + error.responseText + "\n" + "에러 : " + error.status);
			});			
		}
};

var reloadInfo = {
		selectAll : function() {
			
		}
};

/// Event ///

simulatormain.init();