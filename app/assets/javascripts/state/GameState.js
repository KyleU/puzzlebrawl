/* global define:false */
/* global Phaser:false */
/* global _:false */
define(['utils/Status'], function (Status) {
  'use strict';

  function GameState(id, game) {
    this.id = id;
    Phaser.State.call(this, game);
  }

  GameState.prototype = Object.create(Phaser.State.prototype);
  GameState.prototype.constructor = GameState;

  GameState.prototype.create = function() {
    //console.log(this.constructor.name + ' created.');
  };

  GameState.prototype.onMessage = function(c, v) {
    switch(c) {
      case 'Pong':
        var delta = new Date().getTime() - v.timestamp;
        Status.setLatency(delta);
        break;
      case 'MessageSet':
        var self = this;
        _.each(v.messages, function(message) {
          if(message.c !== undefined) {
            self.onMessage(message.c, message.v);
          } else {
            self.onMessage(message[0].replace('models.', ''), message[1]);
          }
        });
        break;
      default:
        console.warn('Unhandled message [' + c + ']: ' + JSON.stringify(v));
    }
  };

  return GameState;
});
