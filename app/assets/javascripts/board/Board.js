/* global define:false */
/* global Phaser:false */
define(['gem/Gem'], function (Gem) {
  'use strict';

  function Board(model, game) {
    this.key = model.key;
    this.w = model.width;
    this.h = model.height;

    Phaser.Group.call(this, game, null, 'board-' + model.key);
    game.add.existing(this);

    this.bgTileSprite = new Phaser.TileSprite(game, 0, 0, 128 * this.w, 128 * this.h, 'board-bg-a');
    this.bgTileSprite.name = 'background';
    this.add(this.bgTileSprite);

    for(var y = 0; y < this.h; y++) {
      for(var x = 0; x < this.w; x++) {
        var g = model.spaces[x][y];
        if(g !== null) {
          this.addGem(new Gem(g, this.game), x, y);
        }
      }
    }
  }

  Board.prototype = Phaser.Group.prototype;
  Board.prototype.constructor = Board;

  Board.prototype.addGem = function(gem, x, y) {
    gem.x = (x * 128) + 64;
    gem.y = this.height - ((y * 128) + 64);
    this.add(gem);
  };

  Board.prototype.setActiveGems = function(ags) {
    for(var agIdx = 0; agIdx < ags.length; agIdx++) {
      var ag = ags[agIdx];
      this.addGem(new Gem(ag.gem, this.game), ag.x, ag.y);
    }
  };

  return Board;
});
