export interface IWatcherSettings {
  globalSettings: IGlobalWatcherSettings;
  spaceSettings: ISpaceWatcherSettings[];
}

export interface IGlobalWatcherSettings {
  excludeGroups: string[];
  excludeUsers: string[];
}

export interface ISpaceWatcherSettings {
  spaceKey: string;
  groups: string[];
  excludeGroups: string[];
  excludeUsers: string[];
}
