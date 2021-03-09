import React, { useState } from 'react';
import reactCSS from 'reactcss';
import { SketchPicker } from 'react-color';

const MyColorPicker = (props) => {
  const [ displayColorPicker, setDisplayColorPicker ] = useState(false);
  const [ color, setColor ] = useState(props.color);

  const handleClick = () => {
    setDisplayColorPicker(!displayColorPicker);
  }

  const handleClose = () => {
    setDisplayColorPicker( false );
  };

  const handleChange = (color) => {
    if(props.parentChange != null){
      props.parentChange(props.myType, color);
    }
    setColor(color.hex);
  };

  let styles = reactCSS({
    'default': {
      color: {
        width: '36px',
        height: '14px',
        borderRadius: '2px',
        background: color
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
      <div style={ styles.swatch } onClick={ handleClick }>
        <div style={ styles.color } />
      </div>
      { displayColorPicker ? <div style={ styles.popover }>
        <div style={ styles.cover } onClick={ handleClose }>
          <SketchPicker color={ color } onChange={ handleChange } />
        </div>
      </div> : null }
    </div>
  );
}
 
export default MyColorPicker;