
var Body = {
  SetColor : function(color){
    document.querySelector('body').style.color = color;
  },
  SetBackgroundColor: function(color) {
    document.querySelector('body').style.backgroundColor = color;
  }
}

var Links = {
  SetColor : function(color){
    $('a').css('color', color);  
  }
}
function day_and_night(self){
  var target = document.querySelector('body')
  var i = 0;
  var alist = document.querySelectorAll('a');
    if (self.value == 'night'){
      Links.SetColor('powderblue');
      Body.SetBackgroundColor('black');
      Body.SetColor('white')
      self.value = 'day';
    } else{
      Links.SetColor('blue');
      Body.SetBackgroundColor('white');
      Body.SetColor('black')
      self.value = 'night';
    }
}
