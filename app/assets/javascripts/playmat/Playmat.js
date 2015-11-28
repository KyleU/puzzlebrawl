/* global define:false */
/* global Phaser:false */
/* global _:false */
define(['board/Board', 'gem/Gem', 'playmat/PlaymatResizer', 'utils/Status'], function (Board, Gem, PlaymatResizer, Status) {
  'use strict';

  var Playmat = function(game) {
    Phaser.Group.call(this, game, null, 'playmat');
    this.game.add.existing(this);

    this.boards = [];
    this.boardsByPlayer = {};

    this.resizer = new PlaymatResizer(this);
  };

  Playmat.prototype = Object.create(Phaser.Group.prototype);
  Playmat.prototype.constructor = Playmat;

  Playmat.prototype.setBrawl = function(brawl) {
    Status.set('scenario', brawl.scenario);
    var playmat = this;
    _.each(brawl.players, function(p) {
      var board = new Board(p.board, playmat.game);
      board.setActiveGems(p.activeGems);
      playmat.boards.push(board);
      playmat.boardsByPlayer[p.id] = board;
      playmat.add(board);
    });
    this.resizer.refreshLayout();
  };

  return Playmat;
});
