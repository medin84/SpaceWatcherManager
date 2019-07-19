import React from "react";
import ReactDOM from "react-dom";

import { WatcherSettingsComponent } from "./modules/watcher/WatcherSettingsComponent";

document.addEventListener("DOMContentLoaded", () => {
  const node = document.getElementById("watcher-settings");
  if (node) {
    ReactDOM.render(<WatcherSettingsComponent />, node);
  }
});
