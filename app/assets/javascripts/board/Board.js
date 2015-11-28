/* global define:false */
/* global Phaser:false */
define(['gem/Gem'], function (Gem) {
  'use strict';

  function Board(model, game) {
    this.key = model.key;
    this.w = model.width;
    this.h = model.height;

    this.activeGems = [];
    this.gems = {};

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
    var original = this.gems[gem.model.id];
    if(original !== null && original !== undefined) {
      throw 'Gem [' + gem.model.id + '] has already been added.';
    }
    gem.x = x * 128;
    gem.y = this.height - (y * 128);
    this.gems[gem.model.id] = gem;
    this.add(gem);
  };

  Board.prototype.moveGem = function(gem, x, y) {
    var original = this.gems[gem.id];
    if(original === null || original === undefined) {
      throw 'Gem [' + gem.model.id + '] has not been added.';
    }
    original.x = x * 128;
    original.y = this.height - (y * 128);
  };

  Board.prototype.removeGem = function(gem) {
    var original = this.gems[gem.id];
    if(original === null || original === undefined) {
      throw 'Gem [' + gem.model.id + '] has not been added.';
    }
    this.gems[gem.id] = null;
    this.remove(gem);
  };

  Board.prototype.setActiveGems = function(ags) {
    for(var agIdx = 0; agIdx < ags.length; agIdx++) {
      var original = this.activeGems[agIdx];
      var ag = ags[agIdx];
      if(original === null || original === undefined) {
        this.addGem(new Gem(ag.gem, this.game), ag.x, ag.y);
      } else {
        if(original.id !== ag.id) {
          throw 'Incorrect active gem at index [' + agIdx + '].';
        }
        this.moveGem(ag.gem, ag.x, ag.y);
      }
    }
    this.activeGems = ags;
  };

  return Board;
});
