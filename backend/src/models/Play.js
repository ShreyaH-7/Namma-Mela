const mongoose = require("mongoose");

const playSchema = new mongoose.Schema(
  {
    title: { type: String, required: true, trim: true },
    genre: { type: String, required: true, trim: true },
    duration: { type: String, required: true, trim: true },
    description: { type: String, required: true, trim: true },
    poster: { type: String, required: true, trim: true },
  },
  { timestamps: true }
);

module.exports = mongoose.model("Play", playSchema);
