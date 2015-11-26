/* global define:false */
/* global Phaser:false */
define(['playmat/PlaymatResizer'], function (PlaymatResizer) {
  'use strict';

  var Playmat = function(client) {
    this.client = client;
    Phaser.Group.call(this, client, null, 'playmat');
    this.game.add.existing(this);

    this.resizer = new PlaymatResizer(this);
    this.resizer.refreshLayout();
  };

  Playmat.prototype = Object.create(Phaser.Group.prototype);
  Playmat.prototype.constructor = Playmat;

  return Playmat;
});
