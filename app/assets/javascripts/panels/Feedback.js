/* global define:false */
define([], function() {
  'use strict';

  function init(game) {
    var promptEl = document.getElementById('feedback-prompt');
    var originalPrompt = promptEl.innerText;

    var contactEl = document.getElementById('feedback-contact');
    var feedbackEl = document.getElementById('feedback-textarea');
    var submitEl = document.getElementById('feedback-submit');
    if(promptEl === null || contactEl === null || feedbackEl === null || submitEl === null) {
      throw new Error('Missing elements');
    }
    submitEl.onclick = function() {
      var msg = {
        contact: contactEl.value,
        feedback: feedbackEl.value
      };

      if(msg.feedback === null || msg.feedback.length === 0) {
        promptEl.innerText = 'Please actually add some feedback. We could use it!';
        setTimeout(function() { promptEl.innerText = originalPrompt; }, 2500);
      } else {
        feedbackEl.value = '';
        game.send('FeedbackResponse', msg);
        promptEl.innerText = 'Thanks for your feedback!';
        setTimeout(function() { promptEl.innerText = originalPrompt; }, 2500);
        setTimeout(function() { window.location = '#menu'; }, 2500);
      }
    };
  }

  return {
    init: init
  };
});
