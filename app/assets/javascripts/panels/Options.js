/* global define:false */
/* global _:false */
define([], function() {
  'use strict';

  function Options(homeState) {
    this.game = homeState.game;
    this.homeState = homeState;
  }

  Options.prototype.loadOptions = function(userPrefs) {
    this.currentOptions = userPrefs;

    var self = this;
    this.elements = document.getElementsByClassName('options-choice');
    _.each(this.elements, function(el) {
      var key = el.dataset.key;
      var value = el.dataset.value;
      el.onclick = function() {
        self.setOption(key, value);
      };
    });
  };

  Options.prototype.setOption = function(key, val) {
    if(this.currentOptions === undefined) {
      throw new Error('Options not initialized.');
    }
    if(this.currentOptions[key] === undefined) {
      throw new Error('Invalid option [' + key + '].');
    }
    var current = this.currentOptions[key];
    if(current !== val) {
      console.log('Changing option [' + key + '] from [' + current + '] to [' + val + '].');
      this.currentOptions[key] = val;
      this.game.send('SetPreference', { 'name': key, 'value': val});

      switch(key) {
        case 'theme':
          document.body.classList.remove('theme-' + current);
          document.body.classList.add('theme-' + val);
          break;
        default:
          break;
      }
    }
  };

  Options.prototype.preferenceChanged = function(name, value, result) {
    switch(name) {
      case 'theme':
        break;
      case 'username':
        this.homeState.chooseName.complete(value, result);
        break;
      default:
        console.log(name, value, result);
        break;
    }
  };

  return Options;
});
