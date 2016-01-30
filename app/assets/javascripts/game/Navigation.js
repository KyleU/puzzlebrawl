/* global define:false */
define(['utils/Sandbox', 'utils/Status'], function (Sandbox, Status) {
  'use strict';

  var states = {
    '': ['Home', 'menu'],
    'home': ['Home', 'menu'],
    'menu': ['Home', 'menu'],
    'instructions': ['Instructions', 'instructions'],
    'test': ['Tests', 'test'],
    'scenario': ['Scenarios', 'scenario'],
    'options': ['Options', 'options'],
    'feedback': ['Feedback', 'feedback'],
    'status': ['Status', 'status']
  };

  function Navigation(game) {
    this.game = game;
    var self = this;

    window.addEventListener('hashchange', function() {
      var h = window.location.hash;
      if(h.charAt(0) === '#') {
        h = h.substr(1);
      }
      self.navigate(h);
      return false;
    }, false);

    var titleLabel = document.getElementById('title-text');
    if(titleLabel !== null && titleLabel !== undefined) {
      titleLabel.addEventListener('click', function() {
        window.location.hash = '#home';
      }, false);
    }
  }

  Navigation.prototype.navigate = function(key) {
    if(this.game.playmat !== undefined && this.game.playmat !== null) {
      if(this.game.playmat.brawl !== undefined && this.game.playmat.brawl !== null) {
        this.game.playmat.resignIfPlaying(key);
        return false;
      }
    }
    if(key === null || key === undefined) {
      throw new Error('Key was not provided.');
    }
    var action = states[key];
    if(action === null || action === undefined) {
      if(key === 'sandbox') {
        Sandbox.go();
      } else {
        Status.setStatus('Starting Brawl');
        this.game.panels.show('gameplay');
        this.game.send('StartBrawl', {'scenario': key});
      }
    } else {
      Status.setStatus(action[0]);
      this.game.panels.show(action[1]);
      if(action[2] !== undefined) {
        action[2](this.game);
      }
    }
    return true;
  };

  return Navigation;
});
