require("dotenv").config();

const cors = require("cors");
const express = require("express");
const morgan = require("morgan");

const connectDatabase = require("./config/db");
const authRoutes = require("./routes/authRoutes");
const playRoutes = require("./routes/playRoutes");
const bookingRoutes = require("./routes/bookingRoutes");
const commentRoutes = require("./routes/commentRoutes");
const castRoutes = require("./routes/castRoutes");

const app = express();
const port = process.env.PORT || 5000;

connectDatabase();

app.use(cors());
app.use(express.json());
app.use(morgan("dev"));

app.get("/api/health", (_req, res) => {
  res.json({ status: "ok", app: "Namma Mela API" });
});

app.use("/api", authRoutes);
app.use("/api", playRoutes);
app.use("/api", bookingRoutes);
app.use("/api", commentRoutes);
app.use("/api", castRoutes);

app.use((err, _req, res, _next) => {
  console.error(err);
  res.status(500).json({ message: "Unexpected server error" });
});

app.listen(port, () => {
  console.log(`Namma Mela backend is running on port ${port}`);
});