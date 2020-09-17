import React from 'react';
import reactCSS from 'reactcss';
import { SketchPicker } from 'react-color';

class MyColorPicker extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      displayColorPicker: false,
      color: this.props.color,
    };
  }

  componentWillReceiveProps(props){
    this.setState({
      color: props.color
    });
  }

  handleClick = () => {
    this.setState({ displayColorPicker: !this.state.displayColorPicker })
  };

  handleClose = () => {
    this.setState({ displayColorPicker: false })
  };

  handleChange = (color) => {
    if(this.props.parentChange != null){
      this.props.parentChange(this.props.myType, color);
    }
    this.setState({ color: color.hex });
  };

  render() {
    var styles = reactCSS({
      'default': {
        color: {
          width: '36px',
          height: '14px',
          borderRadius: '2px',
          background: this.state.color
        },
        swatch: {
          padding: '5px',
          background: '#fff',
          borderRadius: '1px',
          boxShadow: '0 0 0 1px rgba(0,0,0,.1)',
          display: 'inline-block',
          cursor: 'pointer',
        },
        popover: {
          position: 'absolute',
          zIndex: '2',
        },
        cover: {
          position: 'inherit',
          top: '0px',
          right: '175px',
          bottom: '0px',
          left: '-175px',
        },
      },
    });

    return (
      <div>
        <div style={ styles.swatch } onClick={ this.handleClick }>
          <div style={ styles.color } />
        </div>
        { this.state.displayColorPicker ? <div style={ styles.popover }>
          <div style={ styles.cover } onClick={ this.handleClose }>
            <SketchPicker color={ this.state.color } onChange={ this.handleChange } />
          </div>
        </div> : null }

      </div>
    )
  }
}
export default MyColorPicker;