/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Catapult {
        private final DoubleSolenoid catSol;

        public Catapult(DoubleSolenoid sol){
                this.catSol = sol;
        }

        public go(){
                //catapult code goes here
        }

}
