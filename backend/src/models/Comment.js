const mongoose = require("mongoose");

const commentSchema = new mongoose.Schema(
  {
    name: { type: String, required: true, trim: true },
    message: { type: String, required: true, trim: true },
    time: { type: Date, default: Date.now },
  },
  { timestamps: true }
);

module.exports = mongoose.model("Comment", commentSchema);
