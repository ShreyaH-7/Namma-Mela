const express = require("express");
const { getCast, addCast } = require("../controllers/castController");
const { requireAuth, requireAdmin } = require("../middleware/authMiddleware");

const router = express.Router();

router.get("/cast", requireAuth, getCast);
router.post("/cast", requireAuth, requireAdmin, addCast);

module.exports = router;
