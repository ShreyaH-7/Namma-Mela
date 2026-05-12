const bcrypt = require("bcryptjs");
const User = require("../models/User");
const { createToken } = require("../utils/token");

const register = async (req, res) => {
  try {
    const { name, email, password } = req.body;

    if (!name || !email || !password) {
      return res.status(400).json({ message: "Name, email and password are required" });
    }

    const existingUser = await User.findOne({ email: email.toLowerCase() });
    if (existingUser) {
      return res.status(409).json({ message: "User already exists with this email" });
    }

    const passwordHash = await bcrypt.hash(password, 10);
    const user = await User.create({
      name,
      email: email.toLowerCase(),
      password: passwordHash,
    });

    const token = createToken(user);

    return res.status(201).json({
      token,
      user: {
        id: user._id,
        name: user.name,
        email: user.email,
        role: user.role,
      },
    });
  } catch (error) {
    return res.status(500).json({ message: "Unable to register user", error: error.message });
  }
};

const login = async (req, res) => {
  try {
    const { email, password } = req.body;

    if (!email || !password) {
      return res.status(400).json({ message: "Email and password are required" });
    }

    const user = await User.findOne({ email: email.toLowerCase() });
    if (!user) {
      return res.status(401).json({ message: "Invalid email or password" });
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      return res.status(401).json({ message: "Invalid email or password" });
    }

    const token = createToken(user);

    return res.json({
      token,
      user: {
        id: user._id,
        name: user.name,
        email: user.email,
        role: user.role,
      },
    });
  } catch (error) {
    return res.status(500).json({ message: "Unable to login", error: error.message });
  }
};

const adminLogin = async (req, res) => {
  try {
    const { email, password, pin } = req.body;
    const configuredPin = process.env.ADMIN_PIN;

    if (pin && pin === configuredPin) {
      const admin = await User.findOne({ role: "admin" });
      if (!admin) {
        return res.status(404).json({ message: "Admin account not found. Run seed first." });
      }

      return res.json({
        token: createToken(admin),
        user: {
          id: admin._id,
          name: admin.name,
          email: admin.email,
          role: admin.role,
        },
      });
    }

    if (!email || !password) {
      return res.status(400).json({ message: "Email/password or valid PIN is required" });
    }

    const admin = await User.findOne({ email: email.toLowerCase(), role: "admin" });
    if (!admin) {
      return res.status(401).json({ message: "Invalid admin credentials" });
    }

    const isPasswordValid = await bcrypt.compare(password, admin.password);
    if (!isPasswordValid) {
      return res.status(401).json({ message: "Invalid admin credentials" });
    }

    return res.json({
      token: createToken(admin),
      user: {
        id: admin._id,
        name: admin.name,
        email: admin.email,
        role: admin.role,
      },
    });
  } catch (error) {
    return res.status(500).json({ message: "Unable to login as admin", error: error.message });
  }
};

module.exports = { register, login, adminLogin };
