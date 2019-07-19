import axios from "axios";

import { getAjsContextPath } from "../../utils/jira.utils";
import { IWatcherSettings } from "./IWatcherSettings";

const REST_API = `${getAjsContextPath()}/rest/watcher-manager/1/watcher`;
const HEADER_ATL_XTOKEN_NO_CHECK = {
  headers: {
    "X-Atlassian-Token": "no-check"
  }
};

const fetchSettings = (): Promise<IWatcherSettings> => {
  return axios
    .get<IWatcherSettings>(`${REST_API}/settings`)
    .then(response => response.data);
};

const saveSettings = (cfg: IWatcherSettings): Promise<IWatcherSettings> => {
  return axios
    .post<IWatcherSettings>(
      `${REST_API}/settings`,
      cfg,
      HEADER_ATL_XTOKEN_NO_CHECK
    )
    .then(response => response.data);
};

export const API = {
  fetchSettings,
  saveSettings
};
