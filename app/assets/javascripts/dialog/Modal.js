/* global define:false */
define([], function() {
  var modalElement = document.getElementById('modal-dialog');
  var backdropElement = document.getElementById('modal-backdrop');
  var contentElement = document.getElementById('modal-content');
  var closeElement = document.getElementById('modal-close');
  var continueElement = document.getElementById('modal-button-continue');
  var titleElement = document.getElementById('modal-title');
  var bodyElement = document.getElementById('modal-body');

  var Modal = {
    show: function(title, body) {
      contentElement.style.marginTop = (window.innerHeight / 2 - 200) + 'px';
      contentElement.style.marginLeft = (window.innerWidth / 2 - 200) + 'px';

      titleElement.innerText = title;
      bodyElement.innerText = body;
      bodyElement.scrollTop = 0;

      modalElement.className = 'on';
    },

    hide: function() {
      modalElement.className = 'off';
    }
  };

  function f() {
    Modal.hide();
    return true;
  }

  closeElement.onclick = f;
  continueElement.onclick = f;
  backdropElement.onclick = f;

  return Modal;
});
