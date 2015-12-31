/* global define:false */
/* global Phaser:false */
/* global _:false */
define(['utils/Config'], function (Config) {
  'use strict';

  //var targetAspectRatios = [
  //  9 / 16,
  //  3 / 4,
  //  4 / 3,
  //  16 / 9
  //];

  var marginPx = 32;

  var selfScale = 1;
  var othersScale = 0.8;

  var PlaymatResizer = function(playmat) {
    this.playmat = playmat;
  };

  PlaymatResizer.prototype.closestAspectRatio = function() {
    //var world = this.playmat.game.world;
    //var aspectRatio = world.width / world.height;
    //var closestRatio = Phaser.ArrayUtils.findClosest(aspectRatio, targetAspectRatios);
    //console.log(targetAspectRatios, aspectRatio, closestRatio);
  };

  PlaymatResizer.prototype.refreshLayout = function() {
    var p = this.playmat;

    this.closestAspectRatio();

    var selfId = this.playmat.self;
    if(selfId === null) {
      throw 'No self id.';
    }

    var originalSize = [p.w, p.h];

    var splitPlayers = _.partition(p.players, function(player) { return player.id === selfId; });

    if(splitPlayers.length !== 2 || splitPlayers[0].length !== 1) {
      throw 'Incomplete board definitions';
    }

    var self = splitPlayers[0][0];
    var others = splitPlayers[1];

    var xOffset = marginPx;

    self.labels.resize(xOffset, self.board.w * Config.tile.size * selfScale, marginPx + (self.board.h * Config.tile.size * selfScale));

    self.board.x = xOffset;
    self.board.y = marginPx;

    self.board.scale = { x: selfScale, y: selfScale };

    xOffset += self.board.width + (Config.tile.size / 4);

    _.each(others, function(player) {
      player.labels.resize(xOffset, player.board.w * Config.tile.size * othersScale, marginPx + (player.board.h * Config.tile.size * othersScale));

      player.board.x = xOffset;
      player.board.y = marginPx;

      player.board.scale = { x: othersScale, y: othersScale };

      xOffset += player.board.width + marginPx;
    });

    p.w = xOffset;
    p.h = (self.board.h + 1) * Config.tile.size;

    if(p.w !== originalSize[0] || p.h !== originalSize[1]) {
      this.resize();
    }
  };

  var menubarHeight = 32;

  PlaymatResizer.prototype.resize = function() {
    var p = this.playmat;
    var totalHeight = p.game.world.height;

    var widthRatio = p.game.world.width / p.w;
    var heightRatio = (totalHeight - menubarHeight) / p.h;

    var newPosition = p.position;
    var newScale = p.scale;

    if(widthRatio < heightRatio) {
      newScale = new Phaser.Point(widthRatio, widthRatio);
      var yOffset = ((totalHeight - menubarHeight) - (p.h * widthRatio)) / 2;
      if(yOffset > 0 || p.y !== 0) {
        newPosition = new Phaser.Point(0, menubarHeight + yOffset);
      }
    } else {
      newScale = new Phaser.Point(heightRatio, heightRatio);
      var xOffset = (p.game.world.width - (p.w * heightRatio)) / 2;
      if(xOffset > 0 || p.x !== 0) {
        newPosition = new Phaser.Point(xOffset, menubarHeight);
      }
    }

    if(p.initialized) {
      p.game.add.tween(p.scale).to({x: newScale.x, y: newScale.y}, 500, Phaser.Easing.Quadratic.InOut, true);
      p.game.add.tween(p.position).to({x: newPosition.x, y: newPosition.y}, 500, Phaser.Easing.Quadratic.InOut, true);
    } else {
      p.scale = newScale;
      p.position = newPosition;
      p.initialized = true;
    }
  };

  return PlaymatResizer;
});
