---
name: introducing_dependency
description: How to add a new dependency — use the version catalog (libs.versions.toml), keep version names alphabetically ordered, and prefer the latest version. Use whenever adding, upgrading, or wiring a library or dependency into the build.
---

### Adding new required dependency

# Where to add
- In modern development we used version catalog, you will find it in `../gradle/libs.versions.toml`
- All version name should be alphabetic order

# Dependency version
- When you add a dependency, always aim to use the latest version of it