/* global define:false */
/* global Phaser:false */
/* global _:false */
define(['utils/Config'], function (Config) {
  'use strict';

  var PlaymatTargets = function(playmat) {
    this.playmat = playmat;
    this.sprites = [];
  };

  PlaymatTargets.prototype.refreshTargets = function() {
    var self = this;
    _.each(self.playmat.players, function(player, index) {
      if(self.sprites[index] === undefined) {
        self.sprites[index] = new Phaser.Sprite(self.playmat.game, 0, 0, 'gems', 65);
        self.playmat.add(self.sprites[index]);
      }
    });
  };

  PlaymatTargets.prototype.selectTarget = function(tgt) {
    if(this.playmat.self === tgt) {
      throw 'Cannot select self.';
    }
    if(this.playmat.players[this.playmat.self].target !== tgt) {
      this.playmat.game.send('SelectTarget', { target: tgt });
    }
  };

  PlaymatTargets.prototype.setTarget = function(src, tgt) {
    var player = this.playmat.players[src];
    if(player.target !== tgt) {
      console.log('Target for [' + src + '] changed to  [' + tgt + '].');
      player.target = tgt;
    }
  };

  return PlaymatTargets;
});
