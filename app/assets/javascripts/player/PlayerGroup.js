/* global define:false */
/* global Phaser:false */
define(['board/Board', 'player/PlayerLabels'], function (Board, PlayerLabels) {
  'use strict';

  var PlayerGroup = function(game, player, role, score) {
    Phaser.Group.call(this, game, null, 'player-' + player.name);
    this.game.add.existing(this);

    this.player = player;
    this.role = role;
    this.board = new Board(player.id, player.board, game);
    this.add(this.board);

    this.labels = new PlayerLabels(this, score);
  };

  PlayerGroup.prototype = Object.create(Phaser.Group.prototype);
  PlayerGroup.prototype.constructor = PlayerGroup;

  PlayerGroup.prototype.destroy = function() {
    this.board.destroy();
    this.labels.destroy();
  };

  return PlayerGroup;
});
