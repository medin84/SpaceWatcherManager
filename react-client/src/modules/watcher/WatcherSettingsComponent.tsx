import React, { Component } from "react";

import { API } from "./api.service";
import { IWatcherSettings, ISpaceWatcherSettings } from "./IWatcherSettings";

interface State {
  loading: boolean;
  settings: IWatcherSettings;
}

export class WatcherSettingsComponent extends Component<any, State> {
  constructor(props) {
    super(props);

    this.handleAddCfg = this.handleAddCfg.bind(this);
    this.handleSaveSettings = this.handleSaveSettings.bind(this);
    this.handleChangeSpace = this.handleChangeSpace.bind(this);
    this.handleChangeGroup = this.handleChangeGroup.bind(this);
    this.handleChangeExcludedGroup = this.handleChangeExcludedGroup.bind(this);
    this.handleChangeExcludedUsers = this.handleChangeExcludedUsers.bind(this);
    this.handleChangeGlobalExcludeGroups = this.handleChangeGlobalExcludeGroups.bind(
      this
    );
    this.handleChangeGlobalExcludeUsers = this.handleChangeGlobalExcludeUsers.bind(
      this
    );
    this.handleDeleteCfg = this.handleDeleteCfg.bind(this);
  }

  componentDidMount() {
    this.fetchSettings();
  }

  fetchSettings() {
    this.setState({ loading: true });
    API.fetchSettings()
      .then(settings => {
        this.setState({ settings });
      })
      .finally(() => {
        this.setState({ loading: false });
      });
  }

  handleSaveSettings() {
    let { globalSettings, spaceSettings } = this.state.settings;
    globalSettings.excludeGroups = this.preapreListValueToSave(
      globalSettings.excludeGroups
    );
    globalSettings.excludeUsers = this.preapreListValueToSave(
      globalSettings.excludeUsers
    );

    spaceSettings = spaceSettings.map(cfg => {
      return {
        spaceKey: cfg.spaceKey,
        groups: this.preapreListValueToSave(cfg.groups),
        excludeGroups: this.preapreListValueToSave(cfg.excludeGroups),
        excludeUsers: this.preapreListValueToSave(cfg.excludeUsers)
      };
    });

    API.saveSettings({ globalSettings, spaceSettings })
      .then(settings => {
        this.setState({ settings });
      })
      .catch(e => {
        if (e.response.data.message) {
          alert(`Error: ${e.response.data.message}`);
        }
      });
  }

  preapreListValueToSave(values: string[]) {
    return values.map(it => it.trim()).filter(it => it.length > 0);
  }

  normalizeListValue(value: string) {
    return value.split(",").map(it => it.trim());
  }

  handleChangeGlobalExcludeGroups(value: string) {
    const { globalSettings } = this.state.settings;
    globalSettings.excludeGroups = this.normalizeListValue(value);

    this.setState({ settings: this.state.settings });
  }

  handleChangeGlobalExcludeUsers(value: string) {
    const { globalSettings } = this.state.settings;
    globalSettings.excludeUsers = this.normalizeListValue(value);

    this.setState({ settings: this.state.settings });
  }

  handleAddCfg() {
    const { settings } = this.state;
    settings.spaceSettings.push({
      spaceKey: "",
      groups: [],
      excludeGroups: [],
      excludeUsers: []
    });
    this.setState({ settings });
  }

  handleDeleteCfg(cfg: ISpaceWatcherSettings) {
    const { spaceSettings } = this.state.settings;
    const idx = spaceSettings.findIndex(it => it === cfg);
    spaceSettings.splice(idx, 1);
    this.setState({ settings: this.state.settings });
  }

  handleChangeSpace(cfg: ISpaceWatcherSettings, value: string) {
    cfg.spaceKey = value;
    this.setState({ settings: this.state.settings });
  }

  handleChangeGroup(cfg: ISpaceWatcherSettings, value: string) {
    cfg.groups = this.normalizeListValue(value);
    this.setState({ settings: this.state.settings });
  }

  handleChangeExcludedGroup(cfg: ISpaceWatcherSettings, value: string) {
    cfg.excludeGroups = this.normalizeListValue(value);
    this.setState({ settings: this.state.settings });
  }

  handleChangeExcludedUsers(cfg: ISpaceWatcherSettings, value: string) {
    cfg.excludeUsers = this.normalizeListValue(value);
    this.setState({ settings: this.state.settings });
  }

  render() {
    if (!this.state) {
      return null;
    }

    const { loading, settings } = this.state;
    if (loading) {
      return <div>Loading...</div>;
    }

    const { globalSettings, spaceSettings } = settings;
    const style = {
      width: "100%"
    };

    return (
      <div className="settings-container">
        <section>
          <p>Добавление пользователей в наблюдение за пространством.</p>
          <form className="aui long-label">
            <h2>Global settings</h2>
            <section>
              <div className="field-group">
                <label>Exclude groups (Separator ",")</label>
                <span className="field-value long-field" style={style}>
                  <input
                    value={globalSettings.excludeGroups.join(",")}
                    onChange={e =>
                      this.handleChangeGlobalExcludeGroups(e.target.value)
                    }
                    className="text long-field"
                    style={style}
                  />
                </span>
              </div>
              <div className="field-group">
                <label>Exclude users (Separator ",")</label>
                <span className="field-value long-field" style={style}>
                  <input
                    value={globalSettings.excludeUsers.join(",")}
                    onChange={e =>
                      this.handleChangeGlobalExcludeUsers(e.target.value)
                    }
                    className="text long-field"
                    style={style}
                  />
                </span>
              </div>
            </section>
            <h2>Space settings</h2>
            <table className="aui">
              <thead>
                <tr>
                  <th>Space Key</th>
                  <th>Groups (Separator ",")</th>
                  <th>Exclude groups (Separator ",")</th>
                  <th>Exclude users (Separator ",")</th>
                </tr>
              </thead>
              <tbody>
                {spaceSettings.map(cfg => {
                  return (
                    <tr>
                      <td>
                        <input
                          value={cfg.spaceKey}
                          onChange={e =>
                            this.handleChangeSpace(cfg, e.target.value)
                          }
                          className="text long-field"
                          style={style}
                        />
                      </td>
                      <td>
                        <input
                          value={cfg.groups.join(",")}
                          onChange={e =>
                            this.handleChangeGroup(cfg, e.target.value)
                          }
                          className="text long-field"
                          style={style}
                        />
                      </td>
                      <td>
                        <input
                          value={cfg.excludeGroups.join(",")}
                          onChange={e =>
                            this.handleChangeExcludedGroup(cfg, e.target.value)
                          }
                          className="text long-field"
                          style={style}
                        />
                      </td>
                      <td>
                        <input
                          value={cfg.excludeUsers.join(",")}
                          onChange={e =>
                            this.handleChangeExcludedUsers(cfg, e.target.value)
                          }
                          className="text long-field"
                          style={style}
                        />
                      </td>
                      <td>
                        <button
                          type="button"
                          className="aui-button aui-button-link"
                          onClick={e => this.handleDeleteCfg(cfg)}
                        >
                          <i className="icon icon-remove" />
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
            <div style={{ margin: "0.5em 0px 1.5em 0" }}>
              <button
                type="button"
                className="aui-button"
                onClick={this.handleAddCfg}
              >
                Add
              </button>
            </div>
          </form>
        </section>
        <footer>
          <button
            type="button"
            className="aui-button aui-button-primary"
            onClick={this.handleSaveSettings}
          >
            Save
          </button>
        </footer>
      </div>
    );
  }
}
