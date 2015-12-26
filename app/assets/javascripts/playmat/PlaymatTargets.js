/* global define:false */
/* global Phaser:false */
/* global _:false */
define([], function () {
  'use strict';

  var PlaymatTargets = function(playmat) {
    this.playmat = playmat;
  };

  PlaymatTargets.prototype.refreshTarget = function() {
    if(this.sprite === undefined) {
      this.sprite = new Phaser.Sprite(this.playmat.game, 0, 0, 'gems', 65);
      this.sprite.visible = false;
      this.playmat.add(this.sprite);
    }

    var self = this.playmat.players[this.playmat.self];
    if(self.target !== undefined) {
      var tgtBoard = this.playmat.players[self.target].board;
      this.sprite.x = tgtBoard.x;
      this.sprite.y = tgtBoard.y;
      this.sprite.visible = true;
    }
  };

  PlaymatTargets.prototype.selectTarget = function(tgt) {
    if(this.playmat.self === tgt) {
      throw 'Cannot select self.';
    }
    if(this.playmat.players[this.playmat.self].target !== tgt) {
      this.playmat.game.send('SelectTarget', { target: tgt });
    }
  };

  PlaymatTargets.prototype.selectNextTarget = function() {
    var selfPlayer = this.playmat.players[this.playmat.self];
    var currentIdx = _.findIndex(this.playmat.otherPlayers, function(p) {
      return p.id === selfPlayer.target;
    });
    var targetIdx = (currentIdx + 1) % this.playmat.otherPlayers.length;
    var targetId = this.playmat.otherPlayers[targetIdx].id;
    this.selectTarget(targetId);
  };

  PlaymatTargets.prototype.selectPreviousTarget = function() {
    var selfPlayer = this.playmat.players[this.playmat.self];
    var currentIdx = _.findIndex(this.playmat.otherPlayers, function(p) {
      return p.id === selfPlayer.target;
    });
    var targetIdx = (this.playmat.otherPlayers.length + currentIdx - 1) % this.playmat.otherPlayers.length;
    var targetId = this.playmat.otherPlayers[targetIdx].id;
    this.selectTarget(targetId);
  };

  PlaymatTargets.prototype.setTarget = function(src, tgt) {
    var player = this.playmat.players[src];
    if(player.target !== tgt) {
      console.log('Target for [' + src + '] changed to  [' + tgt + '].');
      player.target = tgt;
    }
    if(this.playmat.self === src) {
      var targetBoard = this.playmat.players[tgt].board;

      var tween = this.playmat.game.add.tween(this.sprite);
      tween.to({ x: targetBoard.x, y: targetBoard.y }, 200, Phaser.Easing.Cubic.Out);
      tween.start();
    }
  };

  return PlaymatTargets;
});
