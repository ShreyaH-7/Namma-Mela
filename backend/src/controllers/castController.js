const Cast = require("../models/Cast");

const getCast = async (_req, res) => {
  try {
    const cast = await Cast.find().sort({ createdAt: -1 });
    return res.json(cast);
  } catch (error) {
    return res.status(500).json({ message: "Unable to fetch cast", error: error.message });
  }
};

const addCast = async (req, res) => {
  try {
    const { name, role, bio, image } = req.body;

    if (!name || !role || !bio || !image) {
      return res.status(400).json({ message: "All cast fields are required" });
    }

    const cast = await Cast.create({ name, role, bio, image });
    return res.status(201).json(cast);
  } catch (error) {
    return res.status(500).json({ message: "Unable to add cast member", error: error.message });
  }
};

module.exports = { getCast, addCast };
