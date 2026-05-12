const express = require("express");
const { getPlay, savePlay } = require("../controllers/playController");
const { requireAuth, requireAdmin } = require("../middleware/authMiddleware");

const router = express.Router();

router.get("/play", requireAuth, getPlay);
router.post("/play", requireAuth, requireAdmin, savePlay);

module.exports = router;
