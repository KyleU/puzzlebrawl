/* global define:false */
/* global Phaser:false */
/* global _:false */
define([], function () {
  'use strict';

  var PlaymatTargets = function(playmat) {
    this.playmat = playmat;
  };

  PlaymatTargets.prototype.refreshTarget = function() {
    if(this.sprite === undefined || this.sprite === null) {
      this.sprite = new Phaser.Sprite(this.playmat.game, 0, 0, 'gems', 65);
      this.sprite.name = 'targeting-reticle';
      this.sprite.visible = false;
      this.playmat.add(this.sprite);
    }

    var self = this.playmat.players[this.playmat.self];
    if(self.target !== undefined && this.playmat.otherPlayers.length > 1) {
      var tgtBoard = this.playmat.players[self.target].board;
      this.sprite.x = tgtBoard.x;
      this.sprite.y = tgtBoard.y;
      this.sprite.visible = true;
    }
  };

  PlaymatTargets.prototype.destroy = function() {
    if(this.sprite !== undefined) {
      this.playmat.remove(this.sprite);
      this.sprite.destroy();
      this.sprite = null;
    }
  };

  PlaymatTargets.prototype.selectTarget = function(tgt) {
    if(this.playmat.self === tgt) {
      throw 'Cannot select self.';
    }
    var target = tgt;
    if(typeof target === 'number') {
      if(target >= this.playmat.otherPlayers.length) {
        return;
      }
      target = this.playmat.otherPlayers[target].id;
    }
    if(this.playmat.players[this.playmat.self].target !== target) {
      this.playmat.game.send('SelectTarget', { target: target });
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
    var playmat = this.playmat;
    var player = playmat.players[src];
    if(player.target !== tgt) {
      player.target = tgt;
      if(playmat.self === src) {
        var targetBoard = playmat.players[tgt].board;
        var sprite = this.sprite;

        var tween = this.playmat.game.add.tween(sprite);
        tween.to({ x: targetBoard.x, y: targetBoard.y }, 200, Phaser.Easing.Cubic.Out);
        tween.onComplete.add(function() {
          var selfTeam = playmat.players[playmat.self].team;
          var targetTeam = playmat.players[tgt].team;
          if(selfTeam === targetTeam) {
            sprite.loadTexture('gems', 66);
          } else {
            sprite.loadTexture('gems', 65);
          }
        });
        tween.start();
      }
    }
  };

  return PlaymatTargets;
});
